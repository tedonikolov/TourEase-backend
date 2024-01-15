package com.tourease.user.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "regular", schema = "public")
@Getter
@Setter
public class Regular {
    @Id
    @Column(name = "user_id")
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    private String country;
    private String gender;

    @OneToOne(mappedBy = "regular", cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private Passport passport;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private User user;
}
