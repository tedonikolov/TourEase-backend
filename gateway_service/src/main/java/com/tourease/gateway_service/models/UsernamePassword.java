package com.tourease.gateway_service.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class UsernamePassword {
    private String username;
    private String password;

    public UsernamePassword(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
