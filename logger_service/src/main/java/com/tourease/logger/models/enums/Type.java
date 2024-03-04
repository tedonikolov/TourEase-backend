package com.tourease.logger.models.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Type {
    LOGIN("Login in the system!"),
    WRONG_PASSWORD("Wrong password!"),
    SEND_PASSWORD_CHANGE("Password change email send!"),
    SEND_PASSPORT_UPDATE("Passport update notify send!"),
    PASSPORT_EXPIRED("Passport date expired!"),
    REGULAR_UPDATED("Regular info updated!"),
    REGULAR_CREATED("Regular info added!"),
    PASSPORT_CREATED("Passport info created!"),
    PASSPORT_UPDATED("Passport info updated!"),
    PROFILE_ACTIVATE("Profile activated!"),
    LOGIN_INACTIVE_PROFILE("Tried to login in inactive profile!"),
    ALREADY_EXISTING_PROFILE("Tried to register already registered profile!"),
    ACTIVATION_LINK_SEND("Activation link send!"),
    PROFILE_CREATED("Registration created!"),
    PASSPORT_COUNTRY_NOT_MATCH("Passport country not match!"),
    UPDATED_OWNER_INFORMATION("Updated owner information."),
    OWNER_CREATED("Owner created.");

    private final String label;

    public static final Map<String, Type> BY_LABEL = new HashMap<>();

    static {
        for (Type e : values()) {
            BY_LABEL.put(e.label.toUpperCase(), e);
        }
    }

    Type(String label) {
        this.label=label;
    }
}
