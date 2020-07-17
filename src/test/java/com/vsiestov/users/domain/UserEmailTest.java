package com.vsiestov.users.domain;

import com.vsiestov.shared.core.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class UserEmailTest {

    @Test
    @DisplayName("It should create a valid value object")
    public void createValidObject() {
        Result<UserEmail> emailResult = UserEmail.create("valerii.siestov@gmail.com");

        assertTrue(emailResult.isSuccess());
        assertFalse(emailResult.isFailure());

        assertEquals(emailResult.getValue().getValue(), "valerii.siestov@gmail.com");
    }

    @Test
    @DisplayName("It should create a invalid value object")
    public void createInvalidObject() {
        Result<UserEmail> emailResult = UserEmail.create("valerii.siestov");

        assertFalse(emailResult.isSuccess());
        assertTrue(emailResult.isFailure());
        assertEquals(emailResult.getMessage(), "Provided email is not valid");
    }
}
