package com.tourease.logger.models.dto;

import com.tourease.logger.models.enums.Type;

import java.time.OffsetDateTime;

public record ChronologyFilter (
        String email,
        Type type,
        OffsetDateTime createdAfter,
        OffsetDateTime createdBefore,
        int page,
        int pageSize
) {
}
