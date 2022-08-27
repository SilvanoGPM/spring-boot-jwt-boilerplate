package com.skyg0d.spring.jwt.exception.details;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionDetails {

    protected String title;

    protected String details;

    protected String developerMessage;

    protected int status;

    protected String timestamp;

    public static ExceptionDetails createExceptionDetails(Exception ex, HttpStatus status) {
        return createExceptionDetails(ex, status, "Internal Error.");
    }

    public static ExceptionDetails createExceptionDetails(Exception ex, HttpStatus status, String exTitle) {
        Throwable cause = ex.getCause();

        String title = cause != null
                ? cause.getMessage()
                : exTitle;

        return ExceptionDetails
                .builder()
                .status(status.value())
                .title(title)
                .details(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .timestamp(LocalDateTime.now().toString())
                .build();
    }

}