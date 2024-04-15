package com.tourease.hotel.models.dto.response;

import java.time.LocalDate;
import java.util.List;

public record FreeRoomCountVO(LocalDate date, List<TypeCount> typesCount) {
}
