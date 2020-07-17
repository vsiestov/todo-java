package com.vsiestov.users.domain;

import com.vsiestov.shared.core.Result;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserNameTest {

    @Test
    @DisplayName("it should create a valid name value object")
    public void createValidName() {
        Result<UserName> result = UserName.create("Valeriy", "Provided first name is not valid", "firstName");

        assertTrue(result.isSuccess());
        assertFalse(result.isFailure());
        assertEquals(result.getValue().getValue(),  "Valeriy");
    }

    @Test
    @DisplayName("it should create an imvalid name value object")
    public void createInvalidName() {
        Result result = UserName.create("V", "Provided first name is not valid", "firstName");

        assertFalse(result.isSuccess());
        assertTrue(result.isFailure());
        assertEquals(result.getMessage(),  "Provided first name is not valid");
    }
}
