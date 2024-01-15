package com.tourease.user.services;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.user.models.dto.response.LoginResponse;
import com.tourease.user.models.entities.User;
import com.tourease.user.models.enums.UserStatus;
import com.tourease.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse authorize(String username, String password){
        User user = findEntity(username);

        if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new CustomException("User is not active", ErrorCode.NotActive);
        }

        boolean validMatch = passwordEncoder.matches(password, user.getPassword());
        if (!validMatch) {
            throw new CustomException("Wrong password", ErrorCode.WrongCredentials);
        }

        return new LoginResponse(username,user.getUserType().name());
    }

    public User findEntity(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomException("User not found", ErrorCode.EntityNotFound));
    }
}
