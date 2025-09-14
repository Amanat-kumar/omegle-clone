package com.omegleclone.service;

import com.omegleclone.model.AppUser;
import com.omegleclone.model.ChatSession;
import com.omegleclone.repository.ChatSessionRepository;
import com.omegleclone.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class ChatService {

	@Autowired
	private AppUserRepository appUserRepository;

	@Autowired
	private ChatSessionRepository chatSessionRepository;

	private final Queue<Long> waitingUsers = new ConcurrentLinkedQueue<>();
	private final Object pairingLock = new Object(); // Add synchronization

	@Transactional
	public ChatSession createRandomChatSession(Long currentUserId) {
	    synchronized (pairingLock) {
	        System.out.println("User " + currentUserId + " attempting to join...");
	        
	        // First check if user is already in an active session
	        List<ChatSession> existingSessions = chatSessionRepository.findActiveSessionsForUser(currentUserId);
	        if (!existingSessions.isEmpty()) {
	            ChatSession activeSession = existingSessions.get(0);
	            System.out.println("✅ User " + currentUserId + " rejoining existing session " + activeSession.getId());
	            return activeSession;
	        }
	        
	        // Remove current user from waiting queue if they were already waiting
	        waitingUsers.remove(currentUserId);
	        
	        // Try to find a waiting user
	        Long waitingUserId = null;
	        Iterator<Long> iterator = waitingUsers.iterator();
	        while (iterator.hasNext()) {
	            Long candidateUserId = iterator.next();
	            if (!candidateUserId.equals(currentUserId)) {
	                waitingUserId = candidateUserId;
	                iterator.remove();
	                break;
	            }
	        }
	        
	        if (waitingUserId != null) {
	            // Create session
	            AppUser user1 = appUserRepository.findById(currentUserId).orElseThrow();
	            AppUser user2 = appUserRepository.findById(waitingUserId).orElseThrow();
	            
	            ChatSession chatSession = new ChatSession();
	            chatSession.setUser1(user1);
	            chatSession.setUser2(user2);
	            chatSession.setStatus("ACTIVE");
	            
	            ChatSession saved = chatSessionRepository.save(chatSession);
	            System.out.println("✅ Created session " + saved.getId() + " pairing users " + currentUserId + " and " + waitingUserId);
	            return saved;
	        } else {
	            // Add to waiting queue
	            waitingUsers.offer(currentUserId);
	            System.out.println("⏳ User " + currentUserId + " added to waiting queue");
	            throw new RuntimeException("Waiting for another user to join...");
	        }
	    }
	}

	// Get a chat session by ID
	public ChatSession getChatSessionById(Long id) {
		return chatSessionRepository.findById(id).orElseThrow(() -> new RuntimeException("Chat session not found"));
	}

	// Get all active chat sessions
	public List<ChatSession> getActiveChatSessions() {
		return chatSessionRepository.findByStatus("ACTIVE");
	}
}
