package com.babakjan.moneybag.service;

import com.babakjan.moneybag.entity.Account;
import com.babakjan.moneybag.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public List<Account> getAll() {
        return accountRepository.findAll();
    }
}
