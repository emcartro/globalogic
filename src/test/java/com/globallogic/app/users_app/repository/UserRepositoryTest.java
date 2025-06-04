package com.globallogic.app.users_app.repository;

import com.globallogic.app.users_app.model.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class UserRepositoryTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);

    @Test
    @DisplayName("findByEmail should return user when email exists")
    void findByEmailShouldReturnUserWhenEmailExists() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userRepository.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
    }

    @Test
    @DisplayName("findByEmail should return empty when email does not exist")
    void findByEmailShouldReturnEmptyWhenEmailDoesNotExist() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userRepository.findByEmail(email);

        assertTrue(result.isEmpty());
    }
}