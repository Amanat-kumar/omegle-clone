package com.omegleclone.repository;

import com.omegleclone.model.Message;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // Find all messages for a specific chat session
    List<Message> findByChatSessionId(Long chatSessionId);
}
