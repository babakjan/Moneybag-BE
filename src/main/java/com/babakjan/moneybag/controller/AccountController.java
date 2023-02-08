package com.babakjan.moneybag.controller;

import com.babakjan.moneybag.dto.account.AccountDto;
import com.babakjan.moneybag.dto.account.CreateAccountRequest;
import com.babakjan.moneybag.exception.AccountNotFoundException;
import com.babakjan.moneybag.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    //get all
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AccountDto> getAccounts() {
        return AccountService.accountsToDtos(accountService.getAll());
    }

    //get by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto getAccountById(@PathVariable Long id) throws AccountNotFoundException {
        return accountService.getById(id).dto();
    }

    //create
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountDto createAccount(@RequestBody CreateAccountRequest request) {
        return accountService.save(request).dto();
    }

    //update
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountDto updateAccount(@RequestBody AccountDto request, @PathVariable Long id)
            throws AccountNotFoundException {
        return accountService.update(id, request).dto();
    }

    //delete by id
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable Long id) throws AccountNotFoundException {
        accountService.deleteById(id);
    }
}
