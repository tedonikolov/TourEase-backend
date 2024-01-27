package com.tourease.logger.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "chronology")
public class Chronology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private OffsetDateTime createdOn;

    @Column(columnDefinition = "text")
    private String log;
}


