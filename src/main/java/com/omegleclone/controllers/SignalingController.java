package com.omegleclone.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Controller
public class SignalingController {

    private static final Logger logger = LoggerFactory.getLogger(SignalingController.class);
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    // Track active sessions for better routing and monitoring
    private final ConcurrentMap<String, SessionInfo> activeSessions = new ConcurrentHashMap<>();
    
    // Handle signaling messages with enhanced logging and error handling
    @MessageMapping("/signal/{sessionId}")
    public void handleSignalingMessage(@Payload String signalPayload, 
                                     @DestinationVariable String sessionId) {
        try {
            logger.info("Received signaling message for session: {}", sessionId);
            logger.debug("Signal payload: {}", signalPayload);
            
            // Parse the signal to extract metadata
            JsonNode signalNode = objectMapper.readTree(signalPayload);
            String signalType = signalNode.has("type") ? signalNode.get("type").asText() : "UNKNOWN";
            Long fromUserId = signalNode.has("fromUserId") ? signalNode.get("fromUserId").asLong() : null;
            
            logger.info("Processing {} signal from user {} for session {}", 
                       signalType, fromUserId, sessionId);
            
            // Update session info
            if (fromUserId != null) {
                updateSessionInfo(sessionId, fromUserId, signalType);
            }
            
            // Broadcast to the specific session topic
            // This ensures all clients subscribed to this session receive the message
            String destination = "/topic/signal/" + sessionId;
            messagingTemplate.convertAndSend(destination, signalPayload);
            
            logger.info("Successfully broadcasted {} signal to {}", signalType, destination);
            
            // Optional: Add metrics/monitoring here for production
            incrementSignalingMetric(signalType);
            
        } catch (Exception e) {
            logger.error("Error processing signaling message for session {}: {}", 
                        sessionId, e.getMessage(), e);
            
            // In production, you might want to send an error response back to the client
            sendErrorResponse(sessionId, "Failed to process signaling message");
        }
    }
    
    // Enhanced message mapping for better debugging and monitoring
    @MessageMapping("/signal/{sessionId}/offer")
    public void handleOffer(@Payload String offerPayload, @DestinationVariable String sessionId) {
        logger.info("Processing OFFER for session: {}", sessionId);
        processAndBroadcast(offerPayload, sessionId, "OFFER");
    }
    
    @MessageMapping("/signal/{sessionId}/answer") 
    public void handleAnswer(@Payload String answerPayload, @DestinationVariable String sessionId) {
        logger.info("Processing ANSWER for session: {}", sessionId);
        processAndBroadcast(answerPayload, sessionId, "ANSWER");
    }
    
    @MessageMapping("/signal/{sessionId}/ice")
    public void handleIceCandidate(@Payload String icePayload, @DestinationVariable String sessionId) {
        logger.debug("Processing ICE candidate for session: {}", sessionId);
        processAndBroadcast(icePayload, sessionId, "ICE");
    }
    
    @MessageMapping("/signal/{sessionId}/leave")
    public void handleLeave(@Payload String leavePayload, @DestinationVariable String sessionId) {
        logger.info("Processing LEAVE for session: {}", sessionId);
        processAndBroadcast(leavePayload, sessionId, "LEAVE");
        
        // Clean up session info
        activeSessions.remove(sessionId);
    }
    
    private void processAndBroadcast(String payload, String sessionId, String signalType) {
        try {
            JsonNode signalNode = objectMapper.readTree(payload);
            Long fromUserId = signalNode.has("fromUserId") ? signalNode.get("fromUserId").asLong() : null;
            
            if (fromUserId != null) {
                updateSessionInfo(sessionId, fromUserId, signalType);
            }
            
            String destination = "/topic/signal/" + sessionId;
            messagingTemplate.convertAndSend(destination, payload);
            
            logger.debug("Broadcasted {} to {}", signalType, destination);
            incrementSignalingMetric(signalType);
            
        } catch (Exception e) {
            logger.error("Error processing {} for session {}: {}", signalType, sessionId, e.getMessage(), e);
            sendErrorResponse(sessionId, "Failed to process " + signalType);
        }
    }
    
    private void updateSessionInfo(String sessionId, Long userId, String signalType) {
        activeSessions.compute(sessionId, (key, sessionInfo) -> {
            if (sessionInfo == null) {
                sessionInfo = new SessionInfo(sessionId);
            }
            sessionInfo.addParticipant(userId);
            sessionInfo.updateLastActivity();
            sessionInfo.incrementSignalCount(signalType);
            return sessionInfo;
        });
    }
    
    private void sendErrorResponse(String sessionId, String errorMessage) {
        try {
            String errorPayload = objectMapper.writeValueAsString(
                new ErrorSignal("ERROR", errorMessage, System.currentTimeMillis())
            );
            messagingTemplate.convertAndSend("/topic/signal/" + sessionId, errorPayload);
        } catch (Exception e) {
            logger.error("Failed to send error response: {}", e.getMessage(), e);
        }
    }
    
    // Metrics for monitoring (implement with your preferred monitoring solution)
    private void incrementSignalingMetric(String signalType) {
        // In production, integrate with Micrometer, Prometheus, or other monitoring tools
        logger.trace("Incrementing metric for signal type: {}", signalType);
    }
    
    // Session information for monitoring and debugging
    private static class SessionInfo {
        private final String sessionId;
        private final ConcurrentMap<Long, Boolean> participants = new ConcurrentHashMap<>();
        private final ConcurrentMap<String, Integer> signalCounts = new ConcurrentHashMap<>();
        private volatile long lastActivity = System.currentTimeMillis();
        private final long createdAt = System.currentTimeMillis();
        
        public SessionInfo(String sessionId) {
            this.sessionId = sessionId;
        }
        
        public void addParticipant(Long userId) {
            participants.put(userId, true);
        }
        
        public void updateLastActivity() {
            this.lastActivity = System.currentTimeMillis();
        }
        
        public void incrementSignalCount(String signalType) {
            signalCounts.merge(signalType, 1, Integer::sum);
        }
        
        public int getParticipantCount() {
            return participants.size();
        }
        
        public boolean isActive() {
            return System.currentTimeMillis() - lastActivity < 300000; // 5 minutes
        }
        
        @Override
        public String toString() {
            return String.format("SessionInfo{sessionId='%s', participants=%d, lastActivity=%d, signalCounts=%s}", 
                               sessionId, participants.size(), lastActivity, signalCounts);
        }
    }
    
    // Error signal structure
    private static class ErrorSignal {
        public final String type;
        public final String message;
        public final long timestamp;
        
        public ErrorSignal(String type, String message, long timestamp) {
            this.type = type;
            this.message = message;
            this.timestamp = timestamp;
        }
    }
    
    // Optional: Endpoint to get session statistics for monitoring
    @MessageMapping("/signal/{sessionId}/stats")
    @SendToUser("/queue/stats")
    public SessionInfo getSessionStats(@DestinationVariable String sessionId) {
        return activeSessions.get(sessionId);
    }
    
    // Cleanup inactive sessions periodically
    // In production, use @Scheduled annotation or a separate cleanup service
    public void cleanupInactiveSessions() {
        activeSessions.entrySet().removeIf(entry -> !entry.getValue().isActive());
    }
}