package com.tourease.user;

import com.tourease.user.repositories.UserRepository;
import com.tourease.user.models.entities.User;
import com.tourease.user.models.enums.UserStatus;
import com.tourease.user.models.enums.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialDataSetup implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initData();
    }

    private void initData() {
        if (userRepository.count() == 0) {
            User user = new User("admin",passwordEncoder.encode("admin"), UserType.ADMIN, UserStatus.ACTIVE);

            userRepository.save(user);
        }
    }
}
