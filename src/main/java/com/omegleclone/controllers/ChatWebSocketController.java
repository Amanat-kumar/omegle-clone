package com.omegleclone.controllers;

import com.omegleclone.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

	@MessageMapping("/chat/{sessionId}")
	@SendTo("/topic/chat/{sessionId}")
	public Message send(Message message) {
		return message; // echo back to all in session
	}
}
