package com.omegleclone.service;

import com.omegleclone.model.ChatSession;
import com.omegleclone.model.Report;
import com.omegleclone.repository.ReportRepository;
import com.omegleclone.repository.ChatSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    // Get all reports
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    // End a chat session
    public void endChatSession(Long chatSessionId) {
        ChatSession session = chatSessionRepository.findById(chatSessionId)
                .orElseThrow(() -> new RuntimeException("Chat session not found"));
        
        session.setStatus("COMPLETED");
        chatSessionRepository.save(session);
    }
}
