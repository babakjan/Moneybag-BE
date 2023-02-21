package com.babakjan.moneybag.controller;

import com.babakjan.moneybag.dto.account.AccountDto;
import com.babakjan.moneybag.dto.user.UpdateUserRequest;
import com.babakjan.moneybag.dto.user.UserDto;
import com.babakjan.moneybag.entity.ErrorMessage;
import com.babakjan.moneybag.entity.TotalAnalytic;
import com.babakjan.moneybag.error.exception.UserNotFoundException;
import com.babakjan.moneybag.service.AccountService;
import com.babakjan.moneybag.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
@RequestMapping(value = "/users", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "User", description = "User information")
@SecurityRequirement(name = "bearer-key")
@ApiResponses({
        @ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized. Authentication is required.",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "403",
                description = "Forbidden. Role USER tries to access or manipulate not their data.",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
        )
})
public class UserController {

    private final UserService userService;

    private final AccountService accountService;

    //get all users
    @GetMapping
    @Secured({"ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return all users.", description = "Role ADMIN is required.")
    public List<UserDto> getAll() {
        return UserService.usersToDtos(userService.getAll());
    }

    //get user by id
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Return user by id.",
            description = "Role ADMIN can get all users. Role USER can get only self."
    )
    public UserDto getById(@PathVariable Long id) throws UserNotFoundException {
        return userService.getById(id).dto();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Delete user by id.",
            description = "All accounts and its records of this user will be also deleted! Role ADMIN can delete all " +
                    "accounts. Role USER can delete only self."
    )
    public void deleteById(@PathVariable Long id) throws UserNotFoundException {
        userService.deleteById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update user by id.",
            description = "Update existing user by id, null or not provided fields are ignored. Role ADMIN can " +
                    "update all users and can set role ADMIN to all users. Role USER can update only self and can't " +
                    "set role ADMIN."
    )
    public UserDto update(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest request)
            throws UserNotFoundException {
        return userService.update(id, request).dto();
    }

    //get accounts by user id
    @GetMapping("/{id}/accounts")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Return user's accounts by user id.",
            description = "Role ADMIN is required or parameter id must belong to authenticated user."
    )
    @Parameter(
            in = ParameterIn.QUERY,
            name = "withIncomesAndExpenses",
            description = "If true, compute incomes and expenses from current month."
    )
    public List<AccountDto> getAccountsByUserId(
            @PathVariable Long id, @RequestParam(required = false, defaultValue = "false") Boolean withIncomesAndExpenses)
            throws UserNotFoundException {
        if (!withIncomesAndExpenses) {
            return AccountService.accountsToDtos(userService.getById(id).getAccounts());
        }
        return accountService.getByAllByUserIdWithThisMontIncomesAndExpenses(id);
    }

    @GetMapping("/{id}/totalAnalytic")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Return total analytic of all accounts.",
            description = "Role ADMIN can access analytic of all users, role USER only of their accounts."
    )
    public TotalAnalytic getTotalAnalytic(@PathVariable Long id) throws UserNotFoundException {
        return userService.getTotalAnalytic(id);
    }
}
