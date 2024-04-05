package com.tourease.authentication.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

@Table(name = "email_request_token")
@Entity
@Getter
@Setter
public class EmailRequestToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String token;
    private OffsetDateTime expirationDate;
    @CreationTimestamp
    private OffsetDateTime createdAt;
}
