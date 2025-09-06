package com.omegleclone.service;

import com.omegleclone.model.Message;
import com.omegleclone.repository.MessageRepository;
import com.omegleclone.repository.ChatSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    // Send a message in an active chat session
    @Transactional
    public Message sendMessage(Message message) {
        // Ensure the chat session exists
        chatSessionRepository.findById(message.getChatSession().getId())
                .orElseThrow(() -> new RuntimeException("Chat session not found"));

        // Save the message
        return messageRepository.save(message);
    }

    // Get all messages from a specific chat session
    public List<Message> getMessagesByChatSessionId(Long chatSessionId) {
        return messageRepository.findByChatSessionId(chatSessionId);
    }
}
