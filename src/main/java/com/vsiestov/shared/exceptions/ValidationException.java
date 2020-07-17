package com.vsiestov.shared.exceptions;

import com.vsiestov.shared.core.ValidationDescription;

import java.util.ArrayList;

public class ValidationException extends RuntimeException {
    private ArrayList<ValidationDescription> errors;

    public ValidationException() {
        this.errors = new ArrayList<>();
    }

    public ValidationException(ArrayList<ValidationDescription> errors) {
        this.errors = errors;
    }

    public ArrayList<ValidationDescription> getErrors() {
        return errors;
    }

    public void addError(ValidationDescription validationDescription) {
        this.errors.add(validationDescription);
    }
}
