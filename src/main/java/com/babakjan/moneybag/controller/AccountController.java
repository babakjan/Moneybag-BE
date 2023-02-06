package com.babakjan.moneybag.controller;

import com.babakjan.moneybag.DTO.AccountDTO;
import com.babakjan.moneybag.DTO.CreateAccountDTO;
import com.babakjan.moneybag.entity.Account;
import com.babakjan.moneybag.exception.AccountNotFoundException;
import com.babakjan.moneybag.exception.RecordNotFoundException;
import com.babakjan.moneybag.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    //get all
    @GetMapping
    public List<Account> getAccounts() {
        return accountService.getAll();
    }

    //get by id
    @GetMapping("/{id}")
    public Account getAccountById(@PathVariable Long id) throws AccountNotFoundException {
        return accountService.getById(id);
    }

    //create
    @PostMapping
    public Account createAccount(@RequestBody CreateAccountDTO createAccountDTO) {
        return accountService.save(createAccountDTO);
    }

    //update
    @PutMapping("/{id}")
    public AccountDTO updateAccount(@RequestBody AccountDTO accountDTO, @PathVariable Long id)
            throws AccountNotFoundException, RecordNotFoundException {
        return accountService.update(id, accountDTO);
    }

    //delete by id
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) throws AccountNotFoundException {
        accountService.deleteById(id);
    }
}
