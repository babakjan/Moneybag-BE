package com.babakjan.moneybag.service;

import com.babakjan.moneybag.dto.account.AccountDto;
import com.babakjan.moneybag.dto.account.CreateAccountRequest;
import com.babakjan.moneybag.dto.account.UpdateAccountRequest;
import com.babakjan.moneybag.entity.Account;
import com.babakjan.moneybag.entity.User;
import com.babakjan.moneybag.exception.AccountNotFoundException;
import com.babakjan.moneybag.exception.UserNotFoundException;
import com.babakjan.moneybag.repository.AccountRepository;
import com.babakjan.moneybag.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final UserRepository userRepository;

    //get all
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    //get by id
    public Account getById(Long id) throws AccountNotFoundException {
        if (id == null) {
            throw new AccountNotFoundException("Account id can't be null.");
        }
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException(id);
        }

        return optionalAccount.get();
    }

    //create
    public Account save(CreateAccountRequest request) throws UserNotFoundException {
        if (request.getUserId() == null) {
            System.out.println("USER ID IS NULL");
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
    public void deleteById(Long id) throws AccountNotFoundException {
        if (id == null) {
            throw new AccountNotFoundException("Account id can't be null.");
        }
        accountRepository.deleteById(id);
    }

    public static List<AccountDto> accountsToDtos(List<Account> accounts) {
        return accounts
                .stream().map(Account::dto)
                .collect(Collectors.toList());
    }
}
