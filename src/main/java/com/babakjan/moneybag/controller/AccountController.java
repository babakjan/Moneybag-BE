package com.babakjan.moneybag.controller;

import com.babakjan.moneybag.dto.account.AccountDto;
import com.babakjan.moneybag.dto.account.CreateAccountRequest;
import com.babakjan.moneybag.dto.account.UpdateAccountRequest;
import com.babakjan.moneybag.entity.ErrorMessage;
import com.babakjan.moneybag.error.exception.AccountNotFoundException;
import com.babakjan.moneybag.error.exception.UserNotFoundException;
import com.babakjan.moneybag.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/accounts", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Account", description = "Financial account")
@SecurityRequirement(name = "bearer-key")
@ApiResponses({
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden. Role USER tries to access or manipulate not their data.",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized. Authentication is required.",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
        )
})
public class AccountController {

    private final AccountService accountService;

    //get all
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ADMIN"})
    @Operation(summary = "Return all accounts.", description = "Role ADMIN is required.")
    public List<AccountDto> getAll() {
        return AccountService.accountsToDtos(accountService.getAll());
    }

    //get by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return account by id.", description = "Role ADMIN is required.")
    public AccountDto getAccountById(@PathVariable Long id) throws AccountNotFoundException, UserNotFoundException {
        return accountService.getById(id).dto();
    }

    //create
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new account.", description = "Role ADMIN is required.")
    public AccountDto createAccount(@RequestBody @Valid CreateAccountRequest request) throws UserNotFoundException {
        return accountService.save(request).dto();
    }

    //delete by id
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Delete account by id.",
            description = "All records in this account will be also deleted! Role ADMIN is required."
    )
    public void deleteById(@PathVariable Long id) throws AccountNotFoundException, UserNotFoundException {
        accountService.deleteById(id);
    }

    //update
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update account by id.",
            description = "Update existing account by id, null or not provided fields are ignored. Role ADMIN is required."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated."),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account or any of it's records not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
            )
    })
    public AccountDto updateAccount(@RequestBody @Valid UpdateAccountRequest request, @PathVariable Long id)
            throws AccountNotFoundException, UserNotFoundException {
        return accountService.update(id, request).dto();
    }
}
