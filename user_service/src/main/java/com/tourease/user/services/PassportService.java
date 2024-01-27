package com.tourease.user.services;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.user.models.dto.request.PassportVO;
import com.tourease.user.models.entities.Passport;
import com.tourease.user.models.entities.User;
import com.tourease.user.repositories.PassportRepository;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PassportService {
    private PassportRepository passportRepository;
    private UserService userService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void save(PassportVO passportVO) {
        User user = userService.findEntity(passportVO.email());

        if (!user.getRegular().getCountry().equals(passportVO.country())) {
            kafkaTemplate.send("user_service", user.getEmail(), "Passport country not match!");
            throw new CustomException("Passport country not the same!", ErrorCode.Failed);
        }

        Passport passport = passportRepository.getPassportById(user.getId());

        if (passport == null) {
            passport = new Passport(user.getRegular(), passportVO);
            kafkaTemplate.send("user_service", user.getEmail(), "Passport info created!");
        }else {
            passport.update(passportVO);
            kafkaTemplate.send("user_service", user.getEmail(), "Passport info updated!");
        }

        passportRepository.save(passport);
    }
}
