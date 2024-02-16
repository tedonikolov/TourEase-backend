package com.tourease.hotel.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "customer", schema = "public")
@Getter
@Setter
public class Customer {
    @Id
    @Column(name = "id", nullable = false)
    private String passportId;
    private String fullName;
    private LocalDate birthDate;
    private String country;
    private String gender;
    private LocalDate creationDate;
    private LocalDate expirationDate;

    @ManyToMany
    @JoinTable(name = "customer_reservations",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "reservations_id"))
    @JsonIgnore
    private Set<Reservation> reservations = new LinkedHashSet<>();

}
