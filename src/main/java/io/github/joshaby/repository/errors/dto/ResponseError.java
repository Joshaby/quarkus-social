package io.github.joshaby.repository.errors.dto;

import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class ResponseError {

    private String message;

    private Collection<FieldError> errors;

    public static <T> ResponseError createFromValidation(String message, Set<ConstraintViolation<T>> violations) {
        List<FieldError> errors = violations.stream().map(
                constraintViolation -> new FieldError(
                        constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage())).toList();

        return new ResponseError(message, errors);
    }
}
