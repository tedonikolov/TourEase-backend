package com.tourease.user.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tourease.user.models.dto.request.RegularVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "regular", schema = "public")
@Getter
@Setter
@NoArgsConstructor
public class Regular {
    @Id
    @Column(name = "user_id")
    private Long id;

    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    private String country;
    private String gender;

    @OneToOne(mappedBy = "regular", cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private Passport passport;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private User user;

    public Regular(User user, RegularVO regularVO) {
        id = user.getId();
        firstName = regularVO.firstName();
        lastName = regularVO.lastName();
        birthDate = regularVO.birthDate();
        country = regularVO.country();
        gender = regularVO.gender();
        this.user = user;
    }

    public void updateRegular(RegularVO update) {
        firstName = update.firstName();
        lastName = update.lastName();
        birthDate = update.birthDate();
        country = update.country();
        gender = update.gender();
    }
}
