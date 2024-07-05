package com.tourease.hotel.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tourease.hotel.models.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "reservation", schema = "public")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Long reservationNumber;

    @CreationTimestamp
    private OffsetDateTime creationDate;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int nights;
    private int peopleCount;
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne()
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private Room room;

    @ManyToOne()
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

    @ManyToOne()
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @ManyToMany(mappedBy = "reservations")
    private Set<Customer> customers = new LinkedHashSet<>();

    @ManyToOne()
    @JoinColumn(name = "worker_id")
    @JsonIgnore
    private Worker worker;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Reservation that = (Reservation) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
