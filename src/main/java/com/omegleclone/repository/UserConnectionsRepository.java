package com.omegleclone.repository;

import com.omegleclone.model.UserConnections;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserConnectionsRepository extends JpaRepository<UserConnections, Long> {

    // Find all connections for a specific user
    List<UserConnections> findByUserId(Long userId);
}
