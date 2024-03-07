package com.tourease.hotel.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "location", schema = "public")
public class Location {

    @Id
    @Column(name = "hotel_id", nullable = false)
    private Long id;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String address;

    private String city;

    private String country;
    @OneToOne()
    @JoinColumn(name = "hotel_id")
    @JsonIgnore
    private Hotel hotel;

}
