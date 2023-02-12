package com.babakjan.moneybag.service;

import com.babakjan.moneybag.dto.user.UpdateUserRequest;
import com.babakjan.moneybag.dto.user.UserDto;
import com.babakjan.moneybag.entity.Role;
import com.babakjan.moneybag.entity.User;
import com.babakjan.moneybag.error.exception.UserNotFoundException;
import com.babakjan.moneybag.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final AuthenticationService authenticationService;

    //get all
    public List<User> getAll() {
        authenticationService.ifNotAdminThrowAccessDenied();

        return userRepository.findAll();
    }

    //get by id
    public User getById(Long id) throws UserNotFoundException {
        authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(id);

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return optionalUser.get();
    }

    //delete user by id
    public void deleteById(Long id) throws UserNotFoundException {
        if (id == null) {
            throw new UserNotFoundException("User's id can't be null.");
        }
        authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(id);

        userRepository.deleteById(id);
    }

    public User update(Long id, UpdateUserRequest request) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        authenticationService.ifNotAdminOrSelfRequestThrowAccessDenied(id);

        if (null != request.getFirstName() && !"".equalsIgnoreCase(request.getFirstName())) {
            optionalUser.get().setFirstName(request.getFirstName());
        }
        if (null != request.getLastName() && !"".equalsIgnoreCase(request.getLastName())) {
            optionalUser.get().setLastName(request.getLastName());
        }
        if (null != request.getEmail() && !"".equalsIgnoreCase(request.getEmail())) {
            optionalUser.get().setEmail(request.getEmail());
        }
        if (null != request.getRole()) {
            if (request.getRole() == Role.ADMIN) {
                authenticationService.ifNotAdminThrowAccessDenied();
            }
            optionalUser.get().setRole(request.getRole());
        }
        userRepository.save(optionalUser.get());
        return optionalUser.get();
    }

    public static List<UserDto> usersToDtos(List<User> users) {
        return users
                .stream().map(User::dto)
                .collect(Collectors.toList());
    }
}
