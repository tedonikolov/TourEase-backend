package com.tourease.configuration.models.entities;

import com.tourease.configuration.models.enums.Field;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "configuration")
@Entity
@Setter
@Getter
@NoArgsConstructor
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true)
    private Field name;
    private String value;

    public Configuration(Field name, String value){
        this.name=name;
        this.value=value;
    }
}
