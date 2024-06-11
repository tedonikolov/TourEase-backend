package com.tourease.logger.models.dto;

import com.tourease.logger.models.collections.Chronology;

import java.time.LocalDateTime;

public record MessageLog(
        String id,
        String email,
        String message,
        LocalDateTime date
) {
    public MessageLog(Chronology chronology) {
        this(chronology.getId(), chronology.getEmail(), chronology.getLog(), chronology.getCreatedOn());
    }
}
