package com.omegleclone.repository;

import com.omegleclone.model.AppUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    // Find a user by their username
    Optional<AppUser> findByUsername(String username);

    // Find a user by their email
    Optional<AppUser> findByEmail(String email);
}
