package com.tourease.logger.models.dto;

import java.time.OffsetDateTime;

public record ChronologyFilter (
        String email,
        OffsetDateTime createdAfter,
        OffsetDateTime createdBefore
) {
}
