package com.globallogic.app.users_app.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GlobalogicExceptionTest {

    @Test
    void constructorWithCodeAndMessageShouldSetBothFields() {
        GlobalogicException exception = new GlobalogicException("USER_001", "User already exists");
        assertEquals("USER_001", exception.getCode());
        assertEquals("User already exists", exception.getMessage());
    }

    @Test
    void noArgsConstructorShouldCreateEmptyException() {
        GlobalogicException exception = new GlobalogicException();
        assertNull(exception.getCode());
        assertNull(exception.getMessage());
    }

    @Test
    void allArgsConstructorShouldSetCode() {
        GlobalogicException exception = new GlobalogicException("USER_001");
        assertEquals("USER_001", exception.getCode());
    }

    @Test
    void constructorShouldHandleNullValues() {
        GlobalogicException exception = new GlobalogicException(null, null);
        assertNull(exception.getCode());
        assertNull(exception.getMessage());
    }

    @Test
    void constructorShouldHandleEmptyStrings() {
        GlobalogicException exception = new GlobalogicException("", "");
        assertEquals("", exception.getCode());
        assertEquals("", exception.getMessage());
    }
}