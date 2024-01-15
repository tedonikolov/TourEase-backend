package com.tourease.user.models.repositories;

import com.tourease.user.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}