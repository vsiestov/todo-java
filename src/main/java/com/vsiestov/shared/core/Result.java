package com.vsiestov.shared.core;

import java.util.ArrayList;

public class Result<T> {
    private boolean isSuccess;
    private T value;
    private String message;
    private String propertyName;

    public Result(boolean isSuccess, String message, T value, String propertyName) {
        if (isSuccess && message != null) {
            throw new Error("InvalidOperation: A result cannot be successful and contain an error");
        }

        if (!isSuccess && message == null) {
            throw new Error("InvalidOperation: A failing result needs to contain an error message");
        }

        this.isSuccess = isSuccess;
        this.message = message;
        this.value = value;
        this.propertyName = propertyName;
    }

    public T getValue() {
        if (!isSuccess) {
            throw new Error("Can't get the value of an error result. Use 'getMessages' instead.");
        }

        return value;
    }

    public String getMessage() {
        return message;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isFailure() {
        return !isSuccess;
    }

    public static <T>Result ok(T value) {
        return new Result<T>(true, null, value, null);
    }

    public static <T>Result fail(String value, String propertyName) {
        return new Result<T>(false, value, null, propertyName);
    }

    public static boolean combine(Result[] list) {
        for (Result result : list) {
            if (result.isFailure()) {
                return false;
            }
        }

        return true;
    }

    public static ArrayList<ValidationDescription> errors(Result[] results) {
        ArrayList<ValidationDescription> list = new ArrayList<>();

        for (Result result : results) {
            if (result.isFailure()) {
                list.add(new ValidationDescription(result.getPropertyName(), result.getMessage()));
            }
        }

        return list;
    }
}
