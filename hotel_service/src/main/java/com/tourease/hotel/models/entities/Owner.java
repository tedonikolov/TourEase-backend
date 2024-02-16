package com.tourease.hotel.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "owner", schema = "public")
@Getter
@Setter
public class Owner {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    private String fullName;

    @Column(unique = true)
    private String companyName;
    private String companyAddress;
    private String phone;
    private String country;
    private String city;
    private String eik;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Hotel> hotels;

}
