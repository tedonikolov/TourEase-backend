package com.tourease.logger.models.dto;

import com.tourease.logger.models.enums.Type;

import java.time.LocalDateTime;

public record ChronologyFilter (
        String email,
        Type type,
        LocalDateTime createdAfter,
        LocalDateTime createdBefore,
        int page,
        int pageSize
) {
}
