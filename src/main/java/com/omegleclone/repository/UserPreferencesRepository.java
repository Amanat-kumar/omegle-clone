package com.omegleclone.repository;

import com.omegleclone.model.UserPreferences;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {
}
