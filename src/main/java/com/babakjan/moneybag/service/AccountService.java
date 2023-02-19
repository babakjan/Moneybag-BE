package com.babakjan.moneybag.service;

import com.babakjan.moneybag.dto.account.AccountDto;
import com.babakjan.moneybag.dto.account.CreateAccountRequest;
import com.babakjan.moneybag.dto.account.UpdateAccountRequest;
import com.babakjan.moneybag.entity.Account;
import com.babakjan.moneybag.entity.Record;
import com.babakjan.moneybag.entity.User;
import com.babakjan.moneybag.error.exception.AccountNotFoundException;
import com.babakjan.moneybag.error.exception.UserNotFoundException;
import com.babakjan.moneybag.repository.AccountRepository;
import com.babakjan.moneybag.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    private final AuthenticationService authenticationService;

    //get all
    public List<Account> getAll() {
        authenticationService.ifNotAdminThrowAccessDenied();
        return accountRepository.findAll();
    }

    //get by id
    public Account getById(Long id) throws AccountNotFoundException, UserNotFoundException {
        if (id == null) {
            throw new AccountNotFoundException("Account id can't be null.");
        }
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException(id);
        }
        authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(optionalAccount.get().getUser().getId());

        return optionalAccount.get();
    }

    //get all accounts by user id with incomes and expenses from current month
    public List<AccountDto> getByAllByUserIdWithThisMontIncomesAndExpenses(Long userId) throws UserNotFoundException {
        authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        List<Account> accounts = optionalUser.get().getAccounts();
        List<AccountDto> accountsDtos = AccountService.accountsToDtos(accounts);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        Date startingDate = calendar.getTime();

        for (int i = 0; i < accounts.size(); i++) {
            List<Record> records = accounts.get(i).getRecords()
                    .stream().filter(record -> record.getDate().after(startingDate)).toList();

            double incomes = 0;
            double expenses = 0;

            for (Record record : records) {
                if (record.getAmount() < 0) {
                    expenses += record.getAmount();
                } else {
                    incomes += record.getAmount();
                }
            }
            accountsDtos.get(i).setIncomes(incomes);
            accountsDtos.get(i).setExpenses(expenses);
        }

        return accountsDtos;
    }

    //create
    public Account save(CreateAccountRequest request) throws UserNotFoundException {
        if (authenticationService.isNotAdminOrSelfRequest(request.getUserId())) {
            throw new AccessDeniedException("Access denied.");
        }
        Optional<User> optionalUser = userRepository.findById(request.getUserId());
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(request.getUserId());
        }
        Account account = new Account(request, optionalUser.get());
        optionalUser.get().addAccount(account);
        return accountRepository.save(account);
    }

    //save
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    //update
    public Account update(Long id, UpdateAccountRequest request) throws AccountNotFoundException, UserNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException("Account of id: " + id + " not found.");
        }
        authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(optionalAccount.get().getUser().getId());

        //if signed user is not admin and trying change user of account
        if (request.getUserId() != null
                && !request.getUserId().equals(optionalAccount.get().getUser().getId())) {
            authenticationService.ifNotAdminThrowAccessDenied();
        }
        if (null != request.getName() && !"".equalsIgnoreCase(request.getName())) {
            optionalAccount.get().setName(request.getName());
        }
        if (null != request.getCurrency() && !"".equalsIgnoreCase(request.getCurrency())) {
            optionalAccount.get().setCurrency(request.getCurrency());
        }
        if (null != request.getBalance()) {
            optionalAccount.get().setBalance(request.getBalance());
        }
        if (null != request.getIcon() && !"".equalsIgnoreCase(request.getIcon())) {
            optionalAccount.get().setIcon(request.getIcon());
        }
        if (null != request.getColor() && !"".equalsIgnoreCase(request.getColor())) {
            optionalAccount.get().setColor(request.getColor());
        }
        if (null != request.getIncludeInStatistic()) {
            optionalAccount.get().setIncludeInStatistic(request.getIncludeInStatistic());
        }
        if (null != request.getUserId()) {
            Optional<User> optionalUser = userRepository.findById(request.getUserId());
            if (optionalUser.isEmpty()) {
                throw new UserNotFoundException(request.getUserId());
            }
            optionalAccount.get().setUser(optionalUser.get());
        }

        accountRepository.save(optionalAccount.get());
        return optionalAccount.get();
    }

    //delete by id
    public void deleteById(Long accountId) throws AccountNotFoundException, AccessDeniedException, UserNotFoundException {
        if (accountId == null) {
            throw new AccountNotFoundException("Account id can't be null.");
        }

        Optional<Account> optionalAccount = accountRepository.findById(accountId);
        if (optionalAccount.isPresent()
                && authenticationService.isNotAdminOrSelfRequest(optionalAccount.get().getUser().getId())) {
            throw new AccessDeniedException("User tried to delete account of other user.");
        }
        accountRepository.deleteById(accountId);
    }

    public static List<AccountDto> accountsToDtos(List<Account> accounts) {
        return accounts.stream().map(Account::dto).toList();
    }
}
