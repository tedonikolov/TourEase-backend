package com.tourease.hotel.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tourease.hotel.models.enums.WorkerType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "worker")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Worker {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    private String fullName;
    private String email;
    @Column(unique = true)
    private String phone;
    @Enumerated(EnumType.STRING)
    private WorkerType workerType;
    @CreationTimestamp
    private LocalDate registrationDate;
    private LocalDate firedDate;

    @ManyToOne()
    @JoinColumn(name = "hotel_id")
    @JsonIgnore
    private Hotel hotel;
}
