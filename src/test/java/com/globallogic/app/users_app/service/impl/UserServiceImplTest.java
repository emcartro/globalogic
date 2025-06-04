package com.globallogic.app.users_app.service.impl;

import com.globallogic.app.users_app.adapter.UserAdapter;
import com.globallogic.app.users_app.exceptions.GlobalogicException;
import com.globallogic.app.users_app.model.dto.request.SignUpRequest;
import com.globallogic.app.users_app.model.dto.response.UserResponse;
import com.globallogic.app.users_app.model.entity.User;
import com.globallogic.app.users_app.repository.UserRepository;
import com.globallogic.app.users_app.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = Mockito.mock(PasswordEncoder.class);
    private final JwtUtil jwtUtil = Mockito.mock(JwtUtil.class);
    private final UserAdapter userAdapter = Mockito.mock(UserAdapter.class);
    private final UserServiceImpl userService = new UserServiceImpl(userRepository, passwordEncoder, jwtUtil, userAdapter);

    @Test
    @DisplayName("signUp should throw exception when email already exists")
    void signUpShouldThrowExceptionWhenEmailAlreadyExists() {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("test@example.com");
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        GlobalogicException exception = assertThrows(GlobalogicException.class, () -> userService.signUp(request));

        assertEquals("USER_001", exception.getCode());
        assertEquals("El usuario con el correo test@example.com ya existe.", exception.getMessage());
    }

    @Test
    @DisplayName("signUp should save user and return UserResponse when request is valid")
    void signUpShouldSaveUserAndReturnUserResponseWhenRequestIsValid() {
        SignUpRequest request = new SignUpRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        User user = new User();
        User savedUser = new User();
        savedUser.setEmail("test@example.com");
        savedUser.setCreated(Instant.now());
        savedUser.setLastLogin(Instant.now());
        UserResponse expectedResponse = new UserResponse();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(userAdapter.toEntity(request)).thenReturn(user);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(savedUser);
        when(jwtUtil.generateToken(savedUser.getEmail())).thenReturn("token");
        when(userAdapter.toResponseWithToken(savedUser, "token")).thenReturn(expectedResponse);

        UserResponse response = userService.signUp(request);

        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("login should throw exception when token is invalid")
    void loginShouldThrowExceptionWhenTokenIsInvalid() {
        String invalidToken = "invalidToken";
        when(jwtUtil.validateToken(invalidToken)).thenReturn(false);

        GlobalogicException exception = assertThrows(GlobalogicException.class, () -> userService.login(invalidToken));

        assertEquals("AUTH_001", exception.getCode());
        assertEquals("Invalid or expired token.", exception.getMessage());
    }

    @Test
    @DisplayName("login should return UserResponse when token is valid")
    void loginShouldReturnUserResponseWhenTokenIsValid() {
        String validToken = "validToken";
        String userEmail = "test@example.com";
        User user = new User();
        user.setEmail(userEmail);
        User updatedUser = new User();
        updatedUser.setEmail(userEmail);
        updatedUser.setLastLogin(Instant.now());
        UserResponse expectedResponse = new UserResponse();

        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.extractUsername(validToken)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(updatedUser);
        when(jwtUtil.generateToken(updatedUser.getEmail())).thenReturn("newToken");
        when(userAdapter.toResponseWithToken(updatedUser, "newToken")).thenReturn(expectedResponse);

        UserResponse response = userService.login(validToken);

        assertEquals(expectedResponse, response);
    }

    @Test
    @DisplayName("login should throw exception when user is not found for valid token")
    void loginShouldThrowExceptionWhenUserIsNotFoundForValidToken() {
        String validToken = "validToken";
        String userEmail = "test@example.com";

        when(jwtUtil.validateToken(validToken)).thenReturn(true);
        when(jwtUtil.extractUsername(validToken)).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        GlobalogicException exception = assertThrows(GlobalogicException.class, () -> userService.login(validToken));

        assertEquals("USER_001", exception.getCode());
        assertEquals("User not found for the provided token.", exception.getMessage());
    }
}