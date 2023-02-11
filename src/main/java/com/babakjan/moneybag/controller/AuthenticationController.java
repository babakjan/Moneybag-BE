package com.babakjan.moneybag.controller;

import com.babakjan.moneybag.dto.user.AuthenticationRequest;
import com.babakjan.moneybag.dto.user.AuthenticationResponse;
import com.babakjan.moneybag.dto.user.RegisterRequest;
import com.babakjan.moneybag.entity.ErrorMessage;
import com.babakjan.moneybag.error.exception.UserAlreadyExistsException;
import com.babakjan.moneybag.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth", produces = "application/json")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and registration")
@ApiResponses({
        @ApiResponse(
                responseCode = "400",
                description = "Bad request. Not valid request.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))
        )
})
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    //authenticate
    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Authenticate existing user.", description = "Return Bearer token")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. Wrong username of password.",
                    content = @Content
            ),
    })
    public AuthenticationResponse authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    //register new user
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register new user.", description = "Return Bearer token")
    @ApiResponses(value = { @ApiResponse(responseCode = "400", description = "User with this email already exists.") })
    public AuthenticationResponse register(@RequestBody @Valid RegisterRequest request)
            throws UserAlreadyExistsException {
        return authenticationService.register(request);
    }

}
