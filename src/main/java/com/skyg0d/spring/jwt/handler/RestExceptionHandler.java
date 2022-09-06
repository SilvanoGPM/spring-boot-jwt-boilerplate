package com.skyg0d.spring.jwt.handler;

import com.skyg0d.spring.jwt.exception.BadRequestException;
import com.skyg0d.spring.jwt.exception.ResourceNotFoundException;
import com.skyg0d.spring.jwt.exception.TokenRefreshException;
import com.skyg0d.spring.jwt.exception.UserAlreadyExistsException;
import com.skyg0d.spring.jwt.exception.details.ExceptionDetails;
import com.skyg0d.spring.jwt.exception.details.ValidationExceptionDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionDetails> handleException(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(ExceptionDetails.createExceptionDetails(ex, status), status);
    }

    @ExceptionHandler(TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionDetails handleTokenRefreshException(TokenRefreshException ex) {
        return ExceptionDetails
                .createExceptionDetails(ex, HttpStatus.FORBIDDEN, "Refresh Token Error");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDetails handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ExceptionDetails
                .createExceptionDetails(ex, HttpStatus.BAD_REQUEST, "Resource Not Found");
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDetails handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return ExceptionDetails
                .createExceptionDetails(ex, HttpStatus.BAD_REQUEST, "User Already Exists");
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDetails handleBadCredentialsException(BadCredentialsException ex) {
        return ExceptionDetails
                .createExceptionDetails(ex, HttpStatus.BAD_REQUEST, "Bad Credentials");
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDetails handleBadRequestException(BadRequestException ex) {
        return ExceptionDetails
                .createExceptionDetails(ex, HttpStatus.BAD_REQUEST, "Bad Request");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionDetails handleAccessDeniedException(AccessDeniedException ex) {
        return ExceptionDetails
                .createExceptionDetails(ex, HttpStatus.FORBIDDEN, "Access Denied");
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(ExceptionDetails.createExceptionDetails(ex, status), status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        Map<String, List<String>> fieldsErrors = getFormattedFieldsErrors(ex.getBindingResult().getFieldErrors());

        ValidationExceptionDetails exceptionDetails = ValidationExceptionDetails
                .builder()
                .title("Check the fields of the sent object.")
                .developerMessage(ex.getClass().getName())
                .details("There was a validation error in one of the object's properties, please enter correct values.")
                .timestamp(LocalDateTime.now().toString())
                .status(status.value())
                .fieldErrors(fieldsErrors)
                .build();

        return new ResponseEntity<>(exceptionDetails, status);
    }

    private Map<String, List<String>> getFormattedFieldsErrors(List<FieldError> fieldErrors) {
        return fieldErrors.stream().collect(Collectors.groupingBy(
                FieldError::getField,
                Collectors.mapping((fieldError -> (Optional
                        .ofNullable(fieldError.getDefaultMessage())
                        .orElse("An error has occurred.")
                )), Collectors.toList())
        ));
    }


}
