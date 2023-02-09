package com.babakjan.moneybag.controller;

import com.babakjan.moneybag.dto.account.AccountDto;
import com.babakjan.moneybag.dto.account.CreateAccountRequest;
import com.babakjan.moneybag.exception.AccountNotFoundException;
import com.babakjan.moneybag.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@Tag(name = "Account", description = "Financial account")
@SecurityRequirement(name = "bearer-key")
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden",
                content = {@Content( mediaType = "application/json", schema = @Schema()) })
})
public class AccountController {

    private final AccountService accountService;

    //get all
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return all accounts.")
    public List<AccountDto> getAccounts() {
        return AccountService.accountsToDtos(accountService.getAll());
    }

    //get by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return account by id.")
    public AccountDto getAccountById(@PathVariable Long id) throws AccountNotFoundException {
        return accountService.getById(id).dto();
    }

    //create
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new account.")
    public AccountDto createAccount(@RequestBody CreateAccountRequest request) {
        return accountService.save(request).dto();
    }

    //delete by id
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete account by id.", description = "All records in this account will be also deleted!")
    public void deleteById(@PathVariable Long id) throws AccountNotFoundException {
        accountService.deleteById(id);
    }

    //update
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update account by id.",
            description = "Update existing account by id, null or not provided fields are ignored."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated."),
            @ApiResponse(responseCode = "404", description = "Account or any of it's records not found.")
    })
    public AccountDto updateAccount(@RequestBody AccountDto request, @PathVariable Long id)
            throws AccountNotFoundException {
        return accountService.update(id, request).dto();
    }
}
