package com.tourease.gateway_service.models;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class UserData extends UsernamePasswordAuthenticationToken {
    private String username;
    private String password;
    public UserData(String username, Object password) {
        super(username,password);
    }

}
