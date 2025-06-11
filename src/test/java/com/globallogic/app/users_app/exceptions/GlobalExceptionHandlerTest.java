package com.globallogic.app.users_app.exceptions;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.globallogic.app.users_app.model.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.validation.ObjectError;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }




    @Test
    void handleHttpMessageNotReadableShouldReturnBadRequestForInvalidFormatException() {
        InvalidFormatException cause = new InvalidFormatException(null, "Invalid value", "value", String.class);
        HttpMessageNotReadableException exception = new HttpMessageNotReadableException("Invalid JSON", cause);

        ResponseEntity<Object> response = globalExceptionHandler.handleHttpMessageNotReadable(
            exception, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertTrue(errorResponse.getError().get(0).getDetail().contains("Invalid format for value 'value'"));
    }

    @Test
    void handleDomusExceptionShouldReturnConflictForUser001Code() {
        GlobalogicException exception = new GlobalogicException("USER_001", "User already exists");

        ResponseEntity<Object> response = globalExceptionHandler.handleDomusException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals(409, errorResponse.getError().get(0).getCodigo());
        assertEquals("User already exists", errorResponse.getError().get(0).getDetail());
    }

    @Test
    void handleDomusExceptionShouldReturnUnauthorizedForAuth001Code() {
        GlobalogicException exception = new GlobalogicException("AUTH_001", "Unauthorized access");

        ResponseEntity<Object> response = globalExceptionHandler.handleDomusException(exception);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals(401, errorResponse.getError().get(0).getCodigo());
    }

    @Test
    void handleDomusExceptionShouldReturnNotFoundForUser002Code() {
        GlobalogicException exception = new GlobalogicException("USER_002", "User not found");

        ResponseEntity<Object> response = globalExceptionHandler.handleDomusException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getError().get(0).getCodigo());
    }

    @Test
    void handleDomusExceptionShouldReturnBadRequestForUnknownCode() {
        GlobalogicException exception = new GlobalogicException("UNKNOWN", "Unknown error");

        ResponseEntity<Object> response = globalExceptionHandler.handleDomusException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals(400, errorResponse.getError().get(0).getCodigo());
    }

    @Test
    void handleAllShouldReturnInternalServerErrorForUnhandledException() {
        Exception exception = new RuntimeException("Unexpected error");

        ResponseEntity<Object> response = globalExceptionHandler.handleAll(exception, webRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertNotNull(errorResponse);
        assertEquals(500, errorResponse.getError().get(0).getCodigo());
        assertEquals("Internal server error. Please try again later.", errorResponse.getError().get(0).getDetail());
    }
}