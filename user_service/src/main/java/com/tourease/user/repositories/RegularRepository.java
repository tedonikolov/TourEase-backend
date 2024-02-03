package com.tourease.user.repositories;

import com.tourease.user.models.entities.Regular;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegularRepository extends JpaRepository<Regular, Long> {
    Regular getRegularById(Long id);
}