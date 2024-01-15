package com.tourease.user;

import com.tourease.user.models.repositories.UserRepository;
import com.tourease.user.models.entities.User;
import com.tourease.user.models.enums.UserStatus;
import com.tourease.user.models.enums.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialDataSetup implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) {
        initData();
    }

    private void initData() {
        if (userRepository.count() == 0) {
            User user = new User("admin","admin", UserType.ADMIN, UserStatus.ACTIVE);

            userRepository.save(user);
        }
    }
}
