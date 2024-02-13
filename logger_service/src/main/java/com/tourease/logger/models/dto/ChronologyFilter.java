package com.tourease.logger.models.dto;

import com.tourease.logger.models.Type;

import java.time.OffsetDateTime;

public record ChronologyFilter (
        String email,
        Type type,
        OffsetDateTime createdAfter,
        OffsetDateTime createdBefore
) {
}
