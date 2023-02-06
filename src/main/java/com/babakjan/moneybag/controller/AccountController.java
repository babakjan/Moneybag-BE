package com.babakjan.moneybag.controller;

import com.babakjan.moneybag.entity.Account;
import com.babakjan.moneybag.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/accounts")
    List<Account> getAccounts() {
        return accountService.getAll();
    }
}
