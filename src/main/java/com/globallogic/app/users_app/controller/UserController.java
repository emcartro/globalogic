package com.globallogic.app.users_app.controller;

import com.globallogic.app.users_app.model.dto.response.UserResponse;
import com.globallogic.app.users_app.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import com.globallogic.app.users_app.model.dto.request.SignUpRequest;


import javax.validation.Valid;

@RestController
public class UserController {


    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        UserResponse response = userService.signUp(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        UserResponse response = userService.login(token);
        return ResponseEntity.ok(response);
    }

}