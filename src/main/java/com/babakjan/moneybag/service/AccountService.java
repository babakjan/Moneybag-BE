package com.babakjan.moneybag.service;

import com.babakjan.moneybag.dto.account.AccountDto;
import com.babakjan.moneybag.dto.account.CreateAccountRequest;
import com.babakjan.moneybag.entity.Account;
import com.babakjan.moneybag.exception.AccountNotFoundException;
import com.babakjan.moneybag.exception.RecordNotFoundException;
import com.babakjan.moneybag.repository.AccountRepository;
import com.babakjan.moneybag.repository.RecordRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final RecordRepository recordRepository;

    //get all
    public List<AccountDto> getAll() {
        return accountRepository.findAll()
                .stream().map(AccountDto::new)
                .collect(Collectors.toList());
    }

    //get by id
    public AccountDto getById(Long id) throws AccountNotFoundException {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if (optionalAccount.isEmpty()) {
            throw new AccountNotFoundException(id);
        }

        return new AccountDto(optionalAccount.get());
    }

    //create
    public AccountDto save(CreateAccountRequest request) {
        return new AccountDto(accountRepository.save(new Account(request)));
    }

    //update
    public AccountDto update(Long id, AccountDto accountDTO) throws AccountNotFoundException, RecordNotFoundException {
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
        return new AccountDto(optionalAccount.get());
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
