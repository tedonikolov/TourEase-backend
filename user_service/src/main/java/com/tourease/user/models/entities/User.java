package com.tourease.user.models.entities;

import com.tourease.user.models.dto.request.UserRegistration;
import com.tourease.user.models.enums.UserStatus;
import com.tourease.user.models.enums.UserType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private UserType userType;
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    private Regular regular;

    public User(String email, String password, UserType userType, UserStatus userStatus) {
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.userStatus = userStatus;
    }

    public User(UserRegistration userRegistration) {
        email = userRegistration.email();
        userType = userRegistration.userType();
        userStatus = UserStatus.PENDING;
    }
}
