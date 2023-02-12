package com.babakjan.moneybag.service;

import com.babakjan.moneybag.authentication.AuthenticationFacadeInterface;
import com.babakjan.moneybag.dto.user.AuthenticationRequest;
import com.babakjan.moneybag.dto.user.AuthenticationResponse;
import com.babakjan.moneybag.dto.user.RegisterRequest;
import com.babakjan.moneybag.entity.User;
import com.babakjan.moneybag.error.exception.UserAlreadyExistsException;
import com.babakjan.moneybag.error.exception.UserNotFoundException;
import com.babakjan.moneybag.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final AuthenticationFacadeInterface authenticationFacadeInterface;

    //register new user
    public AuthenticationResponse register(RegisterRequest request) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(request.getEmail());
        }

        var user = new User(request, passwordEncoder);
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    //authenticate user - generate token
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public User checkIfRequestingSelf(Long id) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        if (authenticationFacadeInterface.getAuthentication() == null
                || !optionalUser.get().getEmail().equals(authenticationFacadeInterface.getAuthentication().getName())) {
            throw new AccessDeniedException("Access denied.");
        }
        return optionalUser.get();
    }

    public boolean isNotAdminOrSelfRequest(Long id) throws UserNotFoundException {
        return !authenticationFacadeInterface.isAdmin() && checkIfRequestingSelf(id) == null;
    }

    public void ifNotAdminOrSelfRequestThrowAccessDenied(Long id) throws UserNotFoundException {
        if (isNotAdminOrSelfRequest(id)) {
            throw new AccessDeniedException("Access denied");
        }
    }

    public boolean isAdmin() {
        return authenticationFacadeInterface.isAdmin();
    }

    public void ifNotAdminThrowAccessDenied() {
        if (!isAdmin()) {
            throw new AccessDeniedException("Admin ROLE required.");
        }
    }
}
