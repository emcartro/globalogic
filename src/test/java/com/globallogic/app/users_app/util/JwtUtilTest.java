package com.globallogic.app.users_app.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private static final String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final long EXPIRATION = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", SECRET_KEY);
        ReflectionTestUtils.setField(jwtUtil, "expiration", EXPIRATION);
    }



    @Test
    void extractClaimShouldThrowExceptionForInvalidToken() {
        String invalidToken = "invalid.token.string";
        assertThrows(Exception.class, () -> jwtUtil.extractUsername(invalidToken));
    }

    @Test
    void generateTokenShouldContainUsernameInClaims() {
        String username = "testUser";
        String token = jwtUtil.generateToken(username);
        assertEquals(username, jwtUtil.extractUsername(token));
    }

    @Test
    void generateTokenShouldHaveCorrectExpirationTime() {
        String token = jwtUtil.generateToken("user");
        Date expiration = jwtUtil.extractExpiration(token);
        Date expectedExpiration = new Date(System.currentTimeMillis() + EXPIRATION);
        assertTrue(Math.abs(expiration.getTime() - expectedExpiration.getTime()) < 1000);
    }

    @Test
    void validateTokenShouldReturnFalseForMalformedToken() {
        String malformedToken = "header.payload";
        assertFalse(jwtUtil.validateToken(malformedToken));
    }

    @Test
    void extractClaimsShouldThrowExceptionForEmptyToken() {
        assertThrows(Exception.class, () -> jwtUtil.extractUsername(""));
    }

    @Test
    void generateTokenShouldCreateDifferentTokensForDifferentUsernames() {
        String token1 = jwtUtil.generateToken("user1");
        String token2 = jwtUtil.generateToken("user2");
        assertNotEquals(token1, token2);
    }

    @Test
    void validateTokenShouldReturnTrueForValidToken() {
        String token = jwtUtil.generateToken("user");
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void validateTokenShouldReturnFalseForExpiredToken() throws InterruptedException {
        ReflectionTestUtils.setField(jwtUtil, "expiration", 1); // 1ms expiration
        String token = jwtUtil.generateToken("user");
        Thread.sleep(2); // Wait for token to expire
        assertFalse(jwtUtil.validateToken(token));
    }

    @Test
    void validateTokenShouldReturnFalseForNullToken() {
        assertFalse(jwtUtil.validateToken(null));
    }

    @Test
    void extractUsernameShouldThrowExceptionForNullToken() {
        assertThrows(Exception.class, () -> jwtUtil.extractUsername(null));
    }

    @Test
    void extractExpirationShouldThrowExceptionForNullToken() {
        assertThrows(Exception.class, () -> jwtUtil.extractExpiration(null));
    }
}