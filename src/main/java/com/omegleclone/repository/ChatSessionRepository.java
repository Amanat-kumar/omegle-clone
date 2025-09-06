package com.omegleclone.repository;

import com.omegleclone.model.ChatSession;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {

    // Find active chat sessions by status
    List<ChatSession> findByStatus(String status);  // Find sessions by status (e.g., "ACTIVE")

    // Find chat session by user
    List<ChatSession> findByUser1_IdOrUser2_Id(Long user1Id, Long user2Id);
}
