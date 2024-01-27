package com.tourease.logger.models.dto;

import com.tourease.logger.models.entities.Chronology;

import java.time.OffsetDateTime;

public record MessageLog(
        Long id,
        String email,
        String message,
        OffsetDateTime date
) {
    public MessageLog(Chronology chronology) {
        this(chronology.getId(), chronology.getEmail(), chronology.getLog(), chronology.getCreatedOn());
    }
}
