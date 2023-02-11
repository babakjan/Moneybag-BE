package com.babakjan.moneybag.controller;

import com.babakjan.moneybag.authentication.AuthenticationFacadeInterface;
import com.babakjan.moneybag.dto.account.AccountDto;
import com.babakjan.moneybag.dto.user.UpdateUserRequest;
import com.babakjan.moneybag.dto.record.RecordDto;
import com.babakjan.moneybag.dto.user.UserDto;
import com.babakjan.moneybag.entity.ErrorMessage;
import com.babakjan.moneybag.entity.Role;
import com.babakjan.moneybag.entity.User;
import com.babakjan.moneybag.error.exception.UserNotFoundException;
import com.babakjan.moneybag.service.AccountService;
import com.babakjan.moneybag.service.RecordService;
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
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "User", description = "User information")
@SecurityRequirement(name = "bearer-key")
@ApiResponses({
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized. Authentication is required.",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
        )
})
public class UserController {

    private final UserService userService;

    private final RecordService recordService;

    private final AccountService accountService;

    private final AuthenticationFacadeInterface authenticationFacadeInterface;

    //get all users
    @GetMapping
    @Secured({"ADMIN"})
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return all users.", description = "Role ADMIN is required.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. Role ADMIN is required.",
                    content = @Content
            ),
    })
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
    @ApiResponses({
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. Role ADMIN can get all users. Role USER can get only self.",
                    content = @Content
            ),
    })
    public UserDto getById(@PathVariable Long id) throws UserNotFoundException {
        //admin has access to all users
        if (authenticationFacadeInterface.isAdmin()) {
            return userService.getById(id).dto();
        }
        //user has access only to self
        return checkIfRequestingSelf(id).dto();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Delete user by id.",
            description = "All accounts and records of this user will be also deleted! Role ADMIN can delete all " +
                    "accounts. Role USER can delete only self."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. Role ADMIN can get all users. Role USER can delete only self.",
                    content = @Content
            ),
    })
    public void deleteById(@PathVariable Long id) throws UserNotFoundException {
        if (authenticationFacadeInterface.isAdmin()) {
            userService.deleteById(id);
            return;
        }
        userService.deleteById(checkIfRequestingSelf(id).getId());
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Update user by id.",
            description = "Update existing user by id, null or not provided fields are ignored. Role ADMIN can " +
                    "update all users and can set role ADMIN to all users. Role USER can update only self and can't " +
                    "set role ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated."),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. When hasn't role ADMIN and trying update other or when user hasn't " +
                            "role ADMIN and trying set it's role to ADMIN.",
                    content = @Content
            ),
    })
    public UserDto update(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest request)
            throws UserNotFoundException {
        if (authenticationFacadeInterface.isAdmin()) {
            return userService.update(id, request).dto();
        }
        //admin role can set only admin
        if (request.getRole() == Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return userService.update(checkIfRequestingSelf(id).getId(), request).dto();
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
    @ApiResponses({
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. Role ADMIN can get all users. Role USER can get only self accounts.",
                    content = @Content
            ),
    })
    public List<AccountDto> getAccountsByUserId(@PathVariable Long id, @RequestParam @Nullable Boolean withIncomesAndExpenses)
            throws UserNotFoundException {
        if (withIncomesAndExpenses == null) {
            withIncomesAndExpenses = false;
        }
        if (authenticationFacadeInterface.isAdmin()) {
            if (withIncomesAndExpenses) {
                return accountService.getByAllByUserIdWithThisMontIncomesAndExpenses(id);
            }
            return AccountService.accountsToDtos(userService.getById(id).getAccounts());
        }
        if (withIncomesAndExpenses) {
            return accountService.getByAllByUserIdWithThisMontIncomesAndExpenses(id);
        }
        return AccountService.accountsToDtos(checkIfRequestingSelf(id).getAccounts());
    }

    //get records by user id (all records from users accounts)
    @GetMapping("/{id}/records")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Return all records from user's accounts by user id.",
            description = "Role ADMIN is required or parameter id must belong to authenticated user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. Role ADMIN can get all users. Role USER can get only self records.",
                    content = @Content
            ),
    })
    public List<RecordDto> getRecordsByUserId(@PathVariable Long id) throws UserNotFoundException {
        if (authenticationFacadeInterface.isAdmin()) {
            return RecordService.recordsToDto(recordService.getRecordsFromUsersAccounts(id));
        }
        return RecordService.recordsToDto(recordService.getRecordsFromUsersAccounts(checkIfRequestingSelf(id).getId()));
    }

    public User checkIfRequestingSelf(Long id) throws UserNotFoundException {
        User user = userService.getById(id);
        if (authenticationFacadeInterface.getAuthentication() == null
                || !user.getEmail().equals(authenticationFacadeInterface.getAuthentication().getName())) {
            throw new AccessDeniedException("Access denied.");
        }
        return user;
    }
}
