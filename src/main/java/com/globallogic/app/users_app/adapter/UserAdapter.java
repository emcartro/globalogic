package com.globallogic.app.users_app.adapter;

import com.globallogic.app.users_app.model.dto.request.SignUpRequest;
import com.globallogic.app.users_app.model.dto.response.UserResponse;
import com.globallogic.app.users_app.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
public class UserAdapter{
    public User toEntity(SignUpRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phones(Optional.ofNullable(request.getPhones()).orElse(Collections.emptyList()))
                .build();
    }

    public UserResponse toResponseWithToken(User user, String token) {
        return UserResponse.builder()
                .id(user.getId())
                .created(user.getCreated())
                .lastLogin(user.getLastLogin())
                .token(token)
                .isActive(user.isActive())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phones(Optional.ofNullable(user.getPhones()).orElse(Collections.emptyList()))
                .build();
    }


}
