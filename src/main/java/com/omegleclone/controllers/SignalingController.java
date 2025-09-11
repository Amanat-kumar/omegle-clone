package com.omegleclone.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class SignalingController {

	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	// Handle signaling messages (offer/answer/ICE)
	@MessageMapping("/signal/{sessionId}")
	public void signalingMessage(String signal,
			@org.springframework.messaging.handler.annotation.DestinationVariable String sessionId) {
		// Broadcast to other user in the session
		messagingTemplate.convertAndSend("/topic/signal/" + sessionId, signal);
	}
}
