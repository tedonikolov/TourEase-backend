package com.tourease.user.models.dto.response;

import com.tourease.user.models.entities.Regular;
import com.tourease.user.models.entities.User;
import com.tourease.user.models.enums.UserType;

public record UserVO(String email, UserType userType, Regular regular) {
    public UserVO(User user){
        this(user.getEmail(), user.getUserType(), user.getRegular());
    }
}
