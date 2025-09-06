package com.omegleclone.controllers;

import com.omegleclone.model.Message;
import com.omegleclone.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    // Send a message in an active chat session
    @PostMapping("/send")
    public Message sendMessage(@RequestBody Message message) {
        return messageService.sendMessage(message);
    }

    // Get all messages from a specific chat session
    @GetMapping("/{chatSessionId}")
    public List<Message> getMessages(@PathVariable Long chatSessionId) {
        return messageService.getMessagesByChatSessionId(chatSessionId);
    }
}
