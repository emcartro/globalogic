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


    }