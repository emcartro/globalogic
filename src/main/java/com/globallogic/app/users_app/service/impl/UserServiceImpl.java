package com.globallogic.app.users_app.service.impl;

import com.globallogic.app.users_app.adapter.UserAdapter;
import com.globallogic.app.users_app.exceptions.GlobalogicException;
import com.globallogic.app.users_app.model.dto.request.SignUpRequest;
import com.globallogic.app.users_app.model.dto.response.UserResponse;
import com.globallogic.app.users_app.model.entity.User;
import com.globallogic.app.users_app.repository.UserRepository;
import com.globallogic.app.users_app.service.UserService;
import com.globallogic.app.users_app.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class UserServiceImpl  implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserAdapter userAdapter;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserAdapter userAdapter) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userAdapter = userAdapter;
    }

    @Transactional
    public UserResponse signUp(SignUpRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new GlobalogicException("USER_001", "El usuario con el correo " + request.getEmail() + " ya existe.");
        }

        User user = userAdapter.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreated(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        user.setLastLogin(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        user.setActive(true);

        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser.getEmail());

       return userAdapter.toResponseWithToken(savedUser, token);
    }

    @Transactional
    public UserResponse login(String requestToken) {
        if (!jwtUtil.validateToken(requestToken)) { // Validate the token from the request header
            throw new GlobalogicException("AUTH_001","Invalid or expired token.");
        }

        String userEmail = jwtUtil.extractUsername(requestToken);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new GlobalogicException("USER_001", "User not found for the provided token."));

        user.setLastLogin(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        User updatedUser = userRepository.save(user);
        String newToken = jwtUtil.generateToken(updatedUser.getEmail());
        return userAdapter.toResponseWithToken(updatedUser, newToken);
    }
}
