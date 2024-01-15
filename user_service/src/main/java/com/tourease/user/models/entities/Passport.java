package com.tourease.user.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Passport {
    @Id
    @Column(name = "regular_id", nullable = false)
    private Long id;
    private String passportId;
    private LocalDate creationDate;
    private LocalDate expirationDate;

    private String country;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "regular_id")
    private Regular regular;
}
