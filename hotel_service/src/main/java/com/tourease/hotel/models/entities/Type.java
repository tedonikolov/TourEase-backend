package com.tourease.hotel.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import com.tourease.hotel.models.enums.Currency;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "type", schema = "public")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToMany
    @JoinTable(name = "type_beds",
            joinColumns = @JoinColumn(name = "type_id"),
            inverseJoinColumns = @JoinColumn(name = "beds_id"))
    private List<Bed> beds = new ArrayList<>() {};

    @OneToMany(mappedBy = "type", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Room> rooms = new LinkedHashSet<>();

    @ManyToOne()
    @JoinColumn(name = "hotel_id")
    @JsonIgnore
    private Hotel hotel;
}
