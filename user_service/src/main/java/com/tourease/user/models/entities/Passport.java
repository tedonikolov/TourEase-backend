package com.tourease.user.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tourease.user.models.dto.request.PassportVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "passport", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class Passport {
    @Id
    @Column(name = "regular_id", nullable = false)
    private Long id;
    private String passportId;
    private LocalDate creationDate;
    private LocalDate expirationDate;
    private Boolean expired;
    private String country;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "regular_id")
    private Regular regular;

    public Passport(Regular regular, PassportVO passportVO) {
        id=regular.getId();
        passportId=passportVO.passportId();
        creationDate=passportVO.creationDate();
        expirationDate=passportVO.expirationDate();
        country=passportVO.country();
        this.regular=regular;
        expired = false;
    }

    public void update(PassportVO updateVO) {
        passportId=updateVO.passportId();
        creationDate=updateVO.creationDate();
        expirationDate=updateVO.expirationDate();
        country=updateVO.country();
        expired = false;
    }
}
