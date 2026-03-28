package com.phdprogress.phd_progress.service;

import com.phdprogress.phd_progress.dto.auth.AuthRequest;
import com.phdprogress.phd_progress.dto.auth.AuthResponse;
import com.phdprogress.phd_progress.dto.user.UserRequest;
import com.phdprogress.phd_progress.dto.user.UserResponse;

public interface AuthService {

    AuthResponse authenticate(AuthRequest request);

    UserResponse register(UserRequest request);
}
