package com.babakjan.moneybag.controller;

import com.babakjan.moneybag.dto.auth.AuthenticationRequest;
import com.babakjan.moneybag.dto.auth.AuthenticationResponse;
import com.babakjan.moneybag.dto.auth.RegisterRequest;
import com.babakjan.moneybag.exception.UserAlreadyExistsException;
import com.babakjan.moneybag.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and registration")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    //authenticate
    @PostMapping("/authenticate")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Authenticate existing user.", description = "Return Bearer token")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    //register new user
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register new user.", description = "Return Bearer token")
    @ApiResponses(value = { @ApiResponse(responseCode = "400", description = "User with this email already exists.") })
    public AuthenticationResponse register(@RequestBody RegisterRequest request) throws UserAlreadyExistsException {
        return authenticationService.register(request);
    }

}
