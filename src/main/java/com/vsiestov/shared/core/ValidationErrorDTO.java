package com.vsiestov.shared.core;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationErrorDTO {
    private final List<ErrorDTO> errors = new ArrayList<>();
    private final int status;

    public final void addError(ErrorDTO errorDTO) {
        errors.add(errorDTO);
    }
}
