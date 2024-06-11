package com.tourease.authentication.collections;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "email_request_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequestToken {
    @Id
    private String id;
    private String email;
    private String token;
    private LocalDateTime expirationDate;
    @CreatedDate
    private LocalDateTime createdAt;
}
