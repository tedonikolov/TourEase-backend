package com.tourease.user.services;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.user.models.dto.request.UserRegistration;
import com.tourease.user.models.dto.response.LoginResponse;
import com.tourease.user.models.entities.User;
import com.tourease.user.models.enums.UserStatus;
import com.tourease.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSenderService emailSenderService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public LoginResponse authorize(String username, String password){
        User user = findEntity(username);

        if (user.getUserStatus() != UserStatus.ACTIVE) {
            kafkaTemplate.send("gateway_service", user.getEmail(), "Tried to login in inactive profile!");
            throw new CustomException("User is not active", ErrorCode.NotActive);
        }

        boolean validMatch = passwordEncoder.matches(password, user.getPassword());
        if (!validMatch) {
            kafkaTemplate.send("gateway_service", user.getEmail(), "Wrong password!");
            throw new CustomException("Wrong password", ErrorCode.WrongCredentials);
        }

        kafkaTemplate.send("gateway_service", user.getEmail(), "Login in the system!");

        return new LoginResponse(username,user.getUserType().name());
    }

    public void register(UserRegistration userRegistration){
        User user = new User(userRegistration);
        user.setPassword(passwordEncoder.encode(userRegistration.password()));

        userRepository.save(user);

        kafkaTemplate.send("user_service", user.getEmail(), "Registration created!");

        emailSenderService.sendActivationMail(user.getEmail());
    }

    public void activateUser(String email) {
        User user = findEntity(email);

        switch (user.getUserStatus()) {
            case ACTIVE -> throw new CustomException("Profile already activated!", ErrorCode.AlreadyActive);
            case INACTIVE -> throw new CustomException("Profile is inactive!", ErrorCode.Inactive);
            case PENDING -> {
                user.setUserStatus(UserStatus.ACTIVE);
                userRepository.save(user);

                kafkaTemplate.send("user_service", user.getEmail(), "Profile activated!");
            }
        }
    }

    public boolean isEmailTaken(String email) {
        kafkaTemplate.send("gateway_service", email, "Tried to register already registered profile!");

        User user = userRepository.getByEmail(email);
        return user != null;
    }

    public User findEntity(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomException("User not found", ErrorCode.EntityNotFound));
    }
}
