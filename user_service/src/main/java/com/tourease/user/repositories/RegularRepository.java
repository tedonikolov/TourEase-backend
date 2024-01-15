package com.tourease.user.repositories;

import com.tourease.user.models.entities.Regular;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegularRepository extends JpaRepository<Regular, Long> {
}