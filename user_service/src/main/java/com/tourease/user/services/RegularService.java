package com.tourease.user.services;

import com.tourease.user.models.dto.request.RegularVO;
import com.tourease.user.models.entities.Regular;
import com.tourease.user.models.entities.User;
import com.tourease.user.repositories.RegularRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegularService {
    private final RegularRepository regularRepository;
    private final UserService userService;

    public void save(RegularVO regularVO) {
        User user = userService.findEntity(regularVO.email());
        Regular regular = regularRepository.getRegularById(user.getId());
        if (regular == null) {
            regular = new Regular(user, regularVO);
            regularRepository.save(regular);
        }else {
            regular.updateRegular(regularVO);
            regularRepository.save(regular);
        }
    }
}
