package com.phdprogress.phd_progress.service.impl;

import com.phdprogress.phd_progress.dto.auth.AuthRequest;
import com.phdprogress.phd_progress.dto.auth.AuthResponse;
import com.phdprogress.phd_progress.dto.user.UserRequest;
import com.phdprogress.phd_progress.dto.user.UserResponse;
import com.phdprogress.phd_progress.security.JwtService;
import com.phdprogress.phd_progress.service.AuthService;
import com.phdprogress.phd_progress.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtService jwtService,
                           UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        String token = jwtService.generateToken(authentication.getName(), authentication.getAuthorities());
        return new AuthResponse(token, "Bearer");
    }

    @Override
    public UserResponse register(UserRequest request) {
        return userService.createUser(request);
    }
}
