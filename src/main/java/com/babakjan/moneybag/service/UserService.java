package com.babakjan.moneybag.service;

import com.babakjan.moneybag.dto.user.UpdateUserRequest;
import com.babakjan.moneybag.dto.user.UserDto;
import com.babakjan.moneybag.entity.Account;
import com.babakjan.moneybag.entity.Role;
import com.babakjan.moneybag.entity.TotalAnalytic;
import com.babakjan.moneybag.entity.User;
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
        Double totalBalance = accountRepository.getTotalBalance(userId); //not in date range
        if (dateLt != null) {
            Double totalIncomesAfterRange = recordRepository.getTotalIncomes(userId, dateLt, new Date());
            Double totalExpenseAfterRange = recordRepository.getTotalExpenses(userId, dateLt, new Date());
            totalBalance += - totalIncomesAfterRange - totalExpenseAfterRange; //in order to get balance from range
        }
        String currency = "";
        List<Account> accounts = accountRepository.findAll();
        if (accounts.size() > 0) {
            currency = accounts.get(0).getCurrency();
        }

        return new TotalAnalytic(totalIncomes, totalExpenses, totalCashFlow, totalBalance, currency);
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
