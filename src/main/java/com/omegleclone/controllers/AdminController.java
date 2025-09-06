package com.omegleclone.controllers;

import com.omegleclone.model.ChatSession;
import com.omegleclone.model.Report;
import com.omegleclone.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	// Get all reports
	@GetMapping("/reports")
	public List<Report> getAllReports() {
		return adminService.getAllReports();
	}

	// End a chat session by ID
	@DeleteMapping("/chats/{id}")
	public void endChatSession(@PathVariable Long id) {
		adminService.endChatSession(id);
	}
}
