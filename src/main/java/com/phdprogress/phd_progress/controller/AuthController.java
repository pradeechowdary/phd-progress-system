package com.phdprogress.phd_progress.controller;

import com.phdprogress.phd_progress.dto.auth.AuthRequest;
import com.phdprogress.phd_progress.dto.auth.AuthResponse;
import com.phdprogress.phd_progress.dto.user.UserRequest;
import com.phdprogress.phd_progress.dto.user.UserResponse;
import com.phdprogress.phd_progress.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        return authService.authenticate(request);
    }
}
