package com.babakjan.moneybag.service;

import com.babakjan.moneybag.dto.user.UserDto;
import com.babakjan.moneybag.entity.User;
import com.babakjan.moneybag.exception.UserNotFoundException;
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

    //get all
    public List<User> getAll() {
        return userRepository.findAll();
    }

    //get by id
    public User getById(Long id) throws UserNotFoundException {
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
        userRepository.deleteById(id);
    }

    public static List<UserDto> usersToDtos(List<User> users) {
        return users
                .stream().map(User::dto)
                .collect(Collectors.toList());
    }
}
