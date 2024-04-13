package com.tourease.hotel.models.dto.response;

import com.tourease.hotel.models.entities.Reservation;
import com.tourease.hotel.models.entities.Room;
import com.tourease.hotel.models.entities.Worker;

public record RoomReservationVO(Room room, Reservation reservation, Worker worker) {
}
