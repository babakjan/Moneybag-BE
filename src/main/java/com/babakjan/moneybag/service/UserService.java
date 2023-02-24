package com.babakjan.moneybag.service;

import com.babakjan.moneybag.dto.user.UpdateUserRequest;
import com.babakjan.moneybag.dto.user.UserDto;
import com.babakjan.moneybag.entity.*;
import com.babakjan.moneybag.error.exception.UserNotFoundException;
import com.babakjan.moneybag.repository.AccountRepository;
import com.babakjan.moneybag.repository.RecordRepository;
import com.babakjan.moneybag.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    private final RecordRepository recordRepository;

    private final AuthenticationService authenticationService;

    /**
     * Get all users. Role ADMIN is required.
     * @return list of users
     */
    public List<User> getAll() {
        authenticationService.ifNotAdminThrowAccessDenied();

        return userRepository.findAll();
    }

    /**
     * Get user by id. Role ADMIN can get all users. Role USER can get only self.
     * @param id user id
     * @return user of specified id
     * @throws UserNotFoundException User of specified id doesn't exist.
     */
    public User getById(Long id) throws UserNotFoundException {
        authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(id);

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return optionalUser.get();
    }

    /**
     * Delete user by id. All accounts and its records of this user will be also deleted! Role ADMIN can delete all
     * users. Role USER can delete only self.
     * @param id user id
     * @throws UserNotFoundException User of specified id doesn't exist.
     */
    public void deleteById(Long id) throws UserNotFoundException {
        if (id == null) {
            throw new UserNotFoundException("User's id can't be null.");
        }
        authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(id);

        userRepository.deleteById(id);
    }

    /**
     * Update user by id. Role ADMIN can update all users and can set role ADMIN to all users. Role USER can update
     * only self and can't set role ADMIN.
     * @param id user id
     * @param request user data (only fields, which will be changed)
     * @return updated user
     * @throws UserNotFoundException User of specified id doesn't exist.
     */
    public User update(Long id, UpdateUserRequest request) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(id);

        if (null != request.getFirstName() && !"".equalsIgnoreCase(request.getFirstName())) {
            optionalUser.get().setFirstName(request.getFirstName());
        }
        if (null != request.getLastName() && !"".equalsIgnoreCase(request.getLastName())) {
            optionalUser.get().setLastName(request.getLastName());
        }
        if (null != request.getEmail() && !"".equalsIgnoreCase(request.getEmail())) {
            optionalUser.get().setEmail(request.getEmail());
        }
        if (null != request.getRole()) {
            if (request.getRole() == Role.ADMIN) {
                authenticationService.ifNotAdminThrowAccessDenied();
            }
            optionalUser.get().setRole(request.getRole());
        }
        userRepository.save(optionalUser.get());
        return optionalUser.get();
    }

    /**
     * Get total analytic (from accounts included in statistic) (incomes, expenses, cash flow...) of user.
     * @param userId user id
     * @param dateGe dateGe date greater or equal than (inclusive)
     * @param dateLt dateLt date lower than (exclusive)
     * @return statistic of specified user
     * @throws UserNotFoundException User of specified id doesn't exist.
     */
    public TotalAnalytic getTotalAnalytic(Long userId, Date dateGe, Date dateLt) throws UserNotFoundException {
        authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(userId);

        Double totalIncomes = recordRepository.getTotalIncomes(userId, dateGe, dateLt);
        Double totalExpenses = recordRepository.getTotalExpenses(userId, dateGe, dateLt);
        Double totalCashFlow = totalIncomes + totalExpenses;
        Double totalBalance = getTotalBalance(userId, dateLt);
        String currency = "";
        List<Account> accounts = accountRepository.findAll();
        if (accounts.size() > 0) {
            currency = accounts.get(0).getCurrency();
        }

        return new TotalAnalytic(totalIncomes, totalExpenses, totalCashFlow, totalBalance, currency);
    }

    /**
     * Get total balance before date. Include only accounts, which are included in statistics.
     * @param userId user id
     * @param dateLt date lower than
     * @return total balance
     * @throws UserNotFoundException User of specified id doesn't exist.
     */
    public Double getTotalBalance(Long userId, Date dateLt) throws UserNotFoundException {
        authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(userId);
        Double totalBalance = accountRepository.getTotalBalance(userId); //not in date range
        if (dateLt != null) {
            Double totalIncomesAfterRange = recordRepository.getTotalIncomes(userId, dateLt, new Date());
            Double totalExpenseAfterRange = recordRepository.getTotalExpenses(userId, dateLt, new Date());
            totalBalance += - totalIncomesAfterRange - totalExpenseAfterRange; //in order to get balance from range
        }
        return totalBalance;
    }

    /**
     * Get time series of balance evolution by user. Include only accounts, which are included in statistics.
     * @param userId user id
     * @param dateGe dateGe dateGe date greater or equal than (inclusive)
     * @param dateLt dateLt date lower than (exclusive)
     * @return time series of total balance evolution
     * @throws UserNotFoundException User of specified id doesn't exist.
     */
    public List<TimeSeriesEntry> getBalanceEvolution(Long userId, Date dateGe, Date dateLt) throws UserNotFoundException {
        authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(userId);

        Double totalBalance = getTotalBalance(userId, dateLt);
        List<TimeSeriesEntry> balanceEvolution =  recordRepository.getSpendingEvolution(userId, dateGe, dateLt);

        for (int i = balanceEvolution.size() - 1; i >= 0; i--) {
            Double tmp = balanceEvolution.get(i).getY();
            balanceEvolution.get(i).setY(totalBalance);
            totalBalance -= tmp;

            //remove time from date (set i to midnight)
            Date dateWithoutTime = balanceEvolution.get(i).getX();
            dateWithoutTime.setHours(0);
            dateWithoutTime.setMinutes(0);

            balanceEvolution.get(0).setX(dateWithoutTime);
        }

        //when no records
        if (balanceEvolution.isEmpty()) {
            balanceEvolution.add(new TimeSeriesEntry(totalBalance, new Date()));
        }

        //add first and last element in order to fill entire interval
        balanceEvolution.add(0, new TimeSeriesEntry(balanceEvolution.get(0).getY(), dateGe));
        Date dateLe = dateLt;
        dateLe.setDate(dateLt.getDate() - 1); //last element must be inclusive
        balanceEvolution.add(new TimeSeriesEntry(balanceEvolution.get(balanceEvolution.size() -1).getY(), dateLe));

        return balanceEvolution;
    }

    /**
     * Map list of users to list of data transfer objects.
     * @param users list of users
     * @return list of user dtos
     */
    public static List<UserDto> usersToDtos(List<User> users) {
        return users.stream().map(User::dto).toList();
    }
}
