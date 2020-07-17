package com.vsiestov.users.domain;

import com.vsiestov.shared.core.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserPasswordTest {

    @Test
    @DisplayName("it should create a valid password object")
    public void createValidPassword() {
        Result<UserPassword> result = UserPassword.create("Password1$");

        assertTrue(result.isSuccess());
        assertFalse(result.isFailure());

        assertEquals(result.getValue().getValue(), "Password1$");
    }

    @Test
    @DisplayName("it should not create a valid password object")
    public void createInvalidPassword() {
        Result<UserPassword> result = UserPassword.create("password");

        assertFalse(result.isSuccess());
        assertTrue(result.isFailure());

        assertEquals(result.getMessage(), "Your password is not valid");
    }
}
