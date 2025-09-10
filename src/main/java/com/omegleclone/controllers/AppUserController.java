package com.omegleclone.controllers;

import com.omegleclone.dto.LoginResponse;
import com.omegleclone.model.AppUser;
import com.omegleclone.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/app-users")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    // Register a new user
    @PostMapping("/register")
    public AppUser registerUser(@RequestBody AppUser user) {
        return appUserService.registerUser(user);
    }

    // Login a user (Return JWT token in a real application)
    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody AppUser user) {
        return appUserService.loginUser(user);  // Generate and return JWT in real-world use
    }

    // Get user details by ID
    @GetMapping("/{id}")
    public AppUser getUser(@PathVariable Long id) {
        return appUserService.getUserById(id);
    }

    // Get list of all users
    @GetMapping("/all")
    public List<AppUser> getAllUsers() {
        return appUserService.getAllUsers();
    }
}
