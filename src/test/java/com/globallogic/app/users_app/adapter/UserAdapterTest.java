package com.globallogic.app.users_app.adapter;

    import com.globallogic.app.users_app.model.dto.request.SignUpRequest;
    import com.globallogic.app.users_app.model.dto.response.UserResponse;
    import com.globallogic.app.users_app.model.entity.Phone;
    import com.globallogic.app.users_app.model.entity.User;
    import org.junit.jupiter.api.DisplayName;
    import org.junit.jupiter.api.Test;

    import java.time.Instant;

    import java.util.List;
    import java.util.UUID;

    import static org.junit.jupiter.api.Assertions.assertEquals;
    import static org.junit.jupiter.api.Assertions.assertTrue;

    class UserAdapterTest {

        private final UserAdapter userAdapter = new UserAdapter();


        @Test
        @DisplayName("toEntity should map SignUpRequest to User with empty phones when phones are null")
        void toEntityShouldMapSignUpRequestToUserWithEmptyPhonesWhenPhonesAreNull() {
            SignUpRequest request = new SignUpRequest();
            request.setName("John Doe");
            request.setEmail("john.doe@example.com");
            request.setPhones(null);

            User user = userAdapter.toEntity(request);

            assertEquals("John Doe", user.getName());
            assertEquals("john.doe@example.com", user.getEmail());
            assertTrue(user.getPhones().isEmpty());
        }

        @Test
        @DisplayName("toResponseWithToken should map User to UserResponse with provided token")
        void toResponseWithTokenShouldMapUserToUserResponseWithProvidedToken() {
            User user = User.builder()
                    .id(UUID.randomUUID())
                    .created(Instant.now())
                    .lastLogin(Instant.now())
                    .isActive(true)
                    .name("John Doe")
                    .email("john.doe@example.com")
                    .password("password123")
                    .build();
            String token = "sampleToken";

            UserResponse response = userAdapter.toResponseWithToken(user, token);

            assertEquals(user.getId(), response.getId());
            assertEquals(user.getCreated(), response.getCreated());
            assertEquals(user.getLastLogin(), response.getLastLogin());
            assertEquals(token, response.getToken());
            assertEquals(user.isActive(), response.isActive());
            assertEquals(user.getName(), response.getName());
            assertEquals(user.getEmail(), response.getEmail());
            assertEquals(user.getPassword(), response.getPassword());

        }



    }