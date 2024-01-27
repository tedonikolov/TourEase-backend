package com.tourease.user.services;

import com.tourease.configuration.exception.CustomException;
import com.tourease.configuration.exception.ErrorCode;
import com.tourease.user.models.dto.request.PassportVO;
import com.tourease.user.models.entities.Passport;
import com.tourease.user.models.entities.User;
import com.tourease.user.repositories.PassportRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PassportService {
    private PassportRepository passportRepository;
    private UserService userService;

    public void save(PassportVO passportVO) {
        User user = userService.findEntity(passportVO.email());

        if (!user.getRegular().getCountry().equals(passportVO.country())) {
            throw new CustomException("Passport country not the same!", ErrorCode.Failed);
        }

        Passport passport = passportRepository.getPassportById(user.getId());

        if (passport == null) {
            passport = new Passport(user.getRegular(), passportVO);
        }else {
            passport.update(passportVO);
        }

        passportRepository.save(passport);
    }
}
