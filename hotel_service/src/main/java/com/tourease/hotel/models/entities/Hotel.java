package com.tourease.hotel.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tourease.hotel.models.enums.Stars;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "hotel", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Stars stars;

    @OneToOne(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private Location location;

    @OneToOne(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Rating rating;

    @ManyToOne()
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Facility> facilities = new LinkedHashSet<>();

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Room> rooms = new LinkedHashSet<>();

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Image> images = new LinkedHashSet<>();

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Reservation> reservations = new LinkedHashSet<>();
}
