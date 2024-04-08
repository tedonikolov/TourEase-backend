package com.tourease.hotel.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tourease.hotel.models.enums.RoomStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "room", schema = "public")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @ManyToMany
    @JoinTable(name = "room_types",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "types_id"))
    private List<Type> types = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "hotel_id")
    @JsonIgnore
    private Hotel hotel;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Reservation> reservations = new LinkedHashSet<>();

    public Room(String name, RoomStatus status, List<Type> types, Hotel hotel) {
        this.name = name;
        this.status = status;
        this.types = types;
        this.hotel = hotel;
    }
}
