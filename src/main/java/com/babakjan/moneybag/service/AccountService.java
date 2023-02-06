package com.babakjan.moneybag.service;

import com.babakjan.moneybag.DTO.AccountDTO;
import com.babakjan.moneybag.DTO.CreateAccountDTO;
import com.babakjan.moneybag.entity.Account;
import com.babakjan.moneybag.exception.AccountNotFoundException;
import com.babakjan.moneybag.exception.RecordNotFoundException;
import com.babakjan.moneybag.repository.AccountRepository;
import com.babakjan.moneybag.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    RecordRepository recordRepository;

    //get all
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    //get by id
    public Account getById(Long id) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException(id);
        }

        return optionalAccount.get();
    }

    //create
    public Account save(CreateAccountDTO createAccountDTO) {
        return accountRepository.save(createAccountDTO.toAccount());
    }

    //update
    public AccountDTO update(Long id, AccountDTO accountDTO) throws AccountNotFoundException, RecordNotFoundException {
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
        return accountDTO;
    }

    //delete by id
    public void deleteById(Long id) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException(id);
        }
        accountRepository.deleteById(id);
    }
}
