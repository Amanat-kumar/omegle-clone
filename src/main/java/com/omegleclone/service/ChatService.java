package com.omegleclone.service;

import com.omegleclone.model.AppUser;
import com.omegleclone.model.ChatSession;
import com.omegleclone.repository.ChatSessionRepository;
import com.omegleclone.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Random;

@Service
public class ChatService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    // Create a random chat session
    @Transactional
    public ChatSession createRandomChatSession() {
        // Get all users
        List<AppUser> users = appUserRepository.findAll();

        if (users.size() < 2) {
            throw new IllegalStateException("Not enough users to create a chat session.");
        }

        // Randomly select two users
        Random random = new Random();
        AppUser user1 = users.get(random.nextInt(users.size()));
        AppUser user2;

        // Ensure user1 is not paired with themselves
        do {
            user2 = users.get(random.nextInt(users.size()));
        } while (user1.getId().equals(user2.getId()));

        // Create and save the chat session
        ChatSession chatSession = new ChatSession();
        chatSession.setUser1(user1);
        chatSession.setUser2(user2);
        chatSession.setStatus("ACTIVE");

        return chatSessionRepository.save(chatSession);
    }

    // Get a chat session by ID
    public ChatSession getChatSessionById(Long id) {
        return chatSessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat session not found"));
    }

    // Get all active chat sessions
    public List<ChatSession> getActiveChatSessions() {
        return chatSessionRepository.findByStatus("ACTIVE");
    }
}
