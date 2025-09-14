package com.omegleclone.controllers;

import com.omegleclone.model.ChatSession;
import com.omegleclone.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // Start a new random video chat session
    @PostMapping("/start")
    public ChatSession startChat(@RequestParam Long userId) { // Add userId parameter
        return chatService.createRandomChatSession(userId);
    }

    // Get chat session details by ID
    @GetMapping("/{id}")
    public ChatSession getChatSession(@PathVariable Long id) {
        return chatService.getChatSessionById(id);
    }

    // Get all active chat sessions
    @GetMapping("/active")
    public List<ChatSession> getActiveChats() {
        return chatService.getActiveChatSessions();
    }
}
