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
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ErrorMessage.class)
                )
        )
})
public class AccountController {

    private final AccountService accountService;

    /**
     * Get all accounts. Role ADMIN is required.
     * @return list of accounts
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Secured({"ADMIN"})
    @Operation(summary = "Return all accounts.", description = "Role ADMIN is required.")
    public List<AccountDto> getAll() {
        return AccountService.accountsToDtos(accountService.getAll());
    }

    /**
     * Get account by id. Role ADMIN can access all accounts, role USER only theirs.
     * @param id account id
     * @return account of specified id
     * @throws AccountNotFoundException Account of specified id doesn't exist.
     * @throws UserNotFoundException Authenticated user doesn't exist.
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Return account by id.",
            description = "Role ADMIN can access all accounts, role USER only their."
    )
    public AccountDto getAccountById(@PathVariable Long id) throws AccountNotFoundException, UserNotFoundException {
        return accountService.getById(id).dto();
    }

    /**
     * Create new account. Role ADMIN can create accounts for all users, role USER only for them.
     * @param request new account data
     * @return created account
     * @throws UserNotFoundException Authenticated user or user form request doesn't exist.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create new account.",
            description = "Role ADMIN can create accounts for all users, role USER only for themself."
    )
    public AccountDto createAccount(@RequestBody @Valid CreateAccountRequest request) throws UserNotFoundException {
        return accountService.save(request).dto();
    }

    /**
     * Delete account by id. All records in this account will be also deleted!Role ADMIN can delete all accounts,
     * role USER only theirs.
     * @param id account id
     * @throws AccountNotFoundException Account of specified id doesn't exist.
     * @throws UserNotFoundException Authenticated user doesn't exist.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Delete account by id.",
            description = "Role ADMIN can delete all accounts, role USER only theirs. All records in this account " +
                    "will be also deleted!"
    )
    public void deleteById(@PathVariable Long id) throws AccountNotFoundException, UserNotFoundException {
        accountService.deleteById(id);
    }

    /**
     * Update account by id. Update existing account by id, null or not provided fields are ignored. Role ADMIN can
     * update all accounts, role USER only theirs.
     * @param request account data (only fields, which will be changed)
     * @param id account id
     * @return updated account
     * @throws AccountNotFoundException Account of specified id doesn't exist.
     * @throws UserNotFoundException Authenticated user or user from request doesn't exist.
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update account by id.",
            description = "Update existing account by id, null or not provided fields are ignored. Role ADMIN can" +
                    "update all accounts, role USER only theirs."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated."),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account or any of it's records not found.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorMessage.class)
                    )
            )
    })
    public AccountDto updateAccount(@RequestBody @Valid UpdateAccountRequest request, @PathVariable Long id)
            throws AccountNotFoundException, UserNotFoundException {
        return accountService.update(id, request).dto();
    }
}
