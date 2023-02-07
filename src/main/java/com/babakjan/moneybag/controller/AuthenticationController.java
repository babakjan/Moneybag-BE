package com.babakjan.moneybag.controller;

import com.babakjan.moneybag.DTO.AuthenticationRequest;
import com.babakjan.moneybag.DTO.AuthenticationResponse;
import com.babakjan.moneybag.DTO.RegisterRequest;
import com.babakjan.moneybag.config.JwtUtils;
import com.babakjan.moneybag.dao.UserDao;
import com.babakjan.moneybag.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        System.out.println("authentication controller");
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        System.out.println("register controller");
        return ResponseEntity.ok(authenticationService.register(request));
    }

}
