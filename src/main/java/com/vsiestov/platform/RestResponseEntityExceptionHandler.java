package com.vsiestov.platform;

import com.vsiestov.shared.core.ErrorDTO;
import com.vsiestov.shared.core.ValidationErrorDTO;
import com.vsiestov.shared.exceptions.ValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ValidationException.class})
    protected final ResponseEntity<Object> handleValidationErrors(final RuntimeException ex, final WebRequest request) {
        final ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO(HttpStatus.UNPROCESSABLE_ENTITY.value());

        ((ValidationException)ex).getErrors()
            .forEach(e -> validationErrorDTO.addError(new ErrorDTO(e.getDescription(), e.getName())));

        return handleExceptionInternal(ex, validationErrorDTO, null, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        final BindingResult result = ex.getBindingResult();
        final List<FieldError> fieldErrors = result.getFieldErrors();
        final ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO(HttpStatus.UNPROCESSABLE_ENTITY.value());

        fieldErrors.forEach(fe -> validationErrorDTO.addError(new ErrorDTO(fe.getDefaultMessage(), fe.getField())));

        return handleExceptionInternal(ex, validationErrorDTO, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }
}
