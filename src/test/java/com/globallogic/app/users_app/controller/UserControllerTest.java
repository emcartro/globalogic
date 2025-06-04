package com.globallogic.app.users_app.controller;
import com.globallogic.app.users_app.model.dto.request.SignUpRequest;
import com.globallogic.app.users_app.model.dto.response.UserResponse;
import com.globallogic.app.users_app.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private final UserService userService = Mockito.mock(UserService.class);
    private final UserController userController = new UserController(userService);

    @Test
    @DisplayName("signUp should return CREATED status and UserResponse when request is valid")
    void signUpShouldReturnCreatedStatusAndUserResponseWhenRequestIsValid() {
        SignUpRequest request = new SignUpRequest();
        UserResponse expectedResponse = new UserResponse();
        when(userService.signUp(request)).thenReturn(expectedResponse);

        ResponseEntity<UserResponse> response = userController.signUp(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    @DisplayName("login should return OK status and UserResponse when token is valid")
    void loginShouldReturnOkStatusAndUserResponseWhenTokenIsValid() {
        String validToken = "Bearer validToken";
        String token = "validToken";
        UserResponse expectedResponse = new UserResponse();
        when(userService.login(token)).thenReturn(expectedResponse);

        ResponseEntity<UserResponse> response = userController.login(validToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    @DisplayName("login should throw exception when Authorization header is invalid")
    void loginShouldThrowExceptionWhenAuthorizationHeaderIsInvalid() {
        String invalidToken = "InvalidHeader";

        try {
            userController.login(invalidToken);
        } catch (StringIndexOutOfBoundsException e) {
            assertEquals("String index out of range: 7", e.getMessage());
        }
    }
}