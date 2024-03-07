package com.tourease.hotel.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "image", schema = "public")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String type;

    @Lob
    private byte[] image;

    private String url;

    @ManyToOne()
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

}
