package com.tourease.core.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "rating")
@Entity
@Getter
@Setter
public class Rating {
    @Id
    @Column(name = "id")
    private Long id;

    private Long hotelId;
    private Integer rate;
    private String comment;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id")
    private Reservation reservation;
}
