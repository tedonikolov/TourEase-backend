package com.tourease.configuration.models.enums;

import java.util.HashMap;
import java.util.Map;

public enum Field {
    EMAIL_FROM("emailFrom"),
    EMAIL_PASSWORD("emailPassword"),
    ACTIVATE_PROFILE_URL("activateProfileURL"),
    PASSPORT_EXPIRED_URL("passportExpiredURL"),
    CHANGE_PASSWORD_URL("changePasswordURL");

    private final String label;
    Field(String label) {
        this.label = label;
    }

    private static final Map<String, Field> BY_LABEL = new HashMap<>();

    static {
        for (Field e : values()) {
            BY_LABEL.put(e.label.toUpperCase(), e);
        }
    }

    public static Field valueOfLabel(String label) {
        if (label == null) return null;
        Field field = BY_LABEL.get(label.toUpperCase());
        if (field == null)
            for (Field value : values()) {
                if (label.toUpperCase().equals(value.toString())) {
                    return value;
                }
            }

        return field;
    }
}
