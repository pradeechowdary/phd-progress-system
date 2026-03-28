package com.phdprogress.phd_progress.service;

import com.phdprogress.phd_progress.dto.user.UserRequest;
import com.phdprogress.phd_progress.dto.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponse createUser(UserRequest request);

    Page<UserResponse> getAllUsers(Pageable pageable);

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long id, UserRequest request);

    void deleteUser(Long id);
}
