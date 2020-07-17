package com.vsiestov.shared.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing Result class")
public class ResultTest {

    @Test
    @DisplayName("it should return a correct result")
    public void getSuccessResult() {
        Result<String> result = Result.ok("Correct value");

        assertTrue(result.isSuccess());
        assertFalse(result.isFailure());
        assertEquals(result.getValue(), "Correct value");
    }

    @Test
    @DisplayName("it should return a failed result")
    public void getFailedResult() {
        Result result = Result.fail("You email is not valid", "email");

        assertTrue(result.isFailure());
        assertFalse(result.isSuccess());
    }

    @Test
    @DisplayName("Combine a couple of result and fail")
    public void combineNegativeResult() {
        assertFalse(Result.combine(new Result[]{
            Result.ok(1),
            Result.ok(2),
            Result.fail("Error", "email")
        }));
    }

    @Test
    @DisplayName("Combine a couple of result and succeed")
    public void combinePositiveResult() {
        assertTrue(Result.combine(new Result[]{
            Result.ok(1),
            Result.ok(2),
            Result.ok(3)
        }));
    }

    @Test
    @DisplayName("Catch an exception while creating a wrong successful result")
    public void successException() {
        Error e = assertThrows(Error.class, () -> {
            new Result(true, "I am an exception method", null, null);
        });

        assertEquals(e.getMessage(), "InvalidOperation: A result cannot be successful and contain an error");
    }

    @Test
    @DisplayName("Catch an exception while creating a wrong failed result")
    public void failedException() {
        Error e = assertThrows(Error.class, () -> {
            new Result(false, null, null, null);
        });

        assertEquals(e.getMessage(), "InvalidOperation: A failing result needs to contain an error message");
    }
}
