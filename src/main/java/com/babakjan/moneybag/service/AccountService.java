package com.babakjan.moneybag.service;

import com.babakjan.moneybag.dto.account.AccountDto;
import com.babakjan.moneybag.dto.account.CreateAccountRequest;
import com.babakjan.moneybag.entity.Account;
import com.babakjan.moneybag.entity.User;
import com.babakjan.moneybag.exception.AccountNotFoundException;
import com.babakjan.moneybag.exception.UserNotFoundException;
import com.babakjan.moneybag.repository.AccountRepository;
import com.babakjan.moneybag.repository.RecordRepository;
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

    private final RecordRepository recordRepository;

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
    public Account update(Long id, AccountDto accountDTO) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException("Account of id: " + id + " not found.");
        }
        if (null != accountDTO.getName() && !"".equalsIgnoreCase(accountDTO.getName())) {
            optionalAccount.get().setName(accountDTO.getName());
        }
        if (null != accountDTO.getCurrency() && !"".equalsIgnoreCase(accountDTO.getCurrency())) {
            optionalAccount.get().setCurrency(accountDTO.getCurrency());
        }
        if (null != accountDTO.getBalance()) {
            optionalAccount.get().setBalance(accountDTO.getBalance());
        }
        if (null != accountDTO.getIcon() && !"".equalsIgnoreCase(accountDTO.getIcon())) {
            optionalAccount.get().setIcon(accountDTO.getIcon());
        }
        if (null != accountDTO.getColor() && !"".equalsIgnoreCase(accountDTO.getColor())) {
            optionalAccount.get().setColor(accountDTO.getColor());
        }
        if (null != accountDTO.getIncludeInStatistic()) {
            optionalAccount.get().setIncludeInStatistic(accountDTO.getIncludeInStatistic());
        }
        if (null != accountDTO.getRecordIds()) {
            optionalAccount.get().setRecords(recordRepository.findAllById(accountDTO.getRecordIds()));
        }

        accountRepository.save(optionalAccount.get());
        return optionalAccount.get();
    }

    //delete by id
    public void deleteById(Long id) throws AccountNotFoundException {
        if (id == null) {
            throw new AccountNotFoundException("Account id can't be null.");
        }
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException(id);
        }
        accountRepository.deleteById(id);
    }

    public static List<AccountDto> accountsToDtos(List<Account> accounts) {
        return accounts
                .stream().map(Account::dto)
                .collect(Collectors.toList());
    }
}
