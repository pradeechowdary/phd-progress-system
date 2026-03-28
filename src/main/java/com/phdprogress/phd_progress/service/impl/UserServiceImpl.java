package com.phdprogress.phd_progress.service.impl;

import com.phdprogress.phd_progress.dto.user.UserRequest;
import com.phdprogress.phd_progress.dto.user.UserResponse;
import com.phdprogress.phd_progress.entity.User;
import com.phdprogress.phd_progress.exception.DuplicateResourceException;
import com.phdprogress.phd_progress.exception.ResourceNotFoundException;
import com.phdprogress.phd_progress.mapper.UserMapper;
import com.phdprogress.phd_progress.repository.UserRepository;
import com.phdprogress.phd_progress.service.AuditLogService;
import com.phdprogress.phd_progress.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuditLogService auditLogService;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UserMapper userMapper,
                           AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.auditLogService = auditLogService;
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        String normalizedRole = request.getRole().toUpperCase();
        validateUniqueness(request.getUsername(), request.getEmail(), null);

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(normalizedRole);

        User savedUser = userRepository.save(user);
        auditLogService.log("USER_CREATED", "USER", savedUser.getId(), currentActor(savedUser.getUsername()), "User created with role " + savedUser.getRole());
        log.info("Created user id={} role={}", savedUser.getId(), savedUser.getRole());
        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        return userMapper.toResponse(findUser(id));
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        User existingUser = findUser(id);
        String normalizedRole = request.getRole().toUpperCase();
        validateUniqueness(request.getUsername(), request.getEmail(), id);

        userMapper.updateEntity(existingUser, request);
        existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        existingUser.setRole(normalizedRole);

        User savedUser = userRepository.save(existingUser);
        auditLogService.log("USER_UPDATED", "USER", savedUser.getId(), currentActor(savedUser.getUsername()), "User details updated");
        log.info("Updated user id={}", savedUser.getId());
        return userMapper.toResponse(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User existingUser = findUser(id);
        auditLogService.log("USER_DELETED", "USER", existingUser.getId(), currentActor(existingUser.getUsername()), "User deleted");
        userRepository.delete(existingUser);
        log.info("Deleted user id={}", id);
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    private void validateUniqueness(String username, String email, Long currentUserId) {
        userRepository.findByUsername(username)
                .filter(user -> currentUserId == null || !user.getId().equals(currentUserId))
                .ifPresent(user -> {
                    throw new DuplicateResourceException("Username already exists");
                });

        userRepository.findByEmailIgnoreCase(email)
                .filter(user -> currentUserId == null || !user.getId().equals(currentUserId))
                .ifPresent(user -> {
                    throw new DuplicateResourceException("Email already exists");
                });
    }

    private String currentActor(String fallback) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return fallback;
    }
}
