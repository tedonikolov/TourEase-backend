package com.tourease.logger.models.collections;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "chronology")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chronology {

    @Id
    private String id;

    private String email;

    private LocalDateTime createdOn;

    private String log;
}


