package com.tourease.hotel.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "bed", schema = "public")
@Getter
@Setter
public class Bed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private int people;
    private BigDecimal price;

    @ManyToMany(mappedBy = "beds")
    @JsonIgnore
    private Set<Type> types = new LinkedHashSet<>();

}
