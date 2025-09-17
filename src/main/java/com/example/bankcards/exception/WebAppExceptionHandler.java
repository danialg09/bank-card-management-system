package com.example.bankcards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WebAppExceptionHandler {

    @ExceptionHandler(value = RefreshTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseBody refreshTokenException(RefreshTokenException e) {
        return buildResponse(e);
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseBody alreadyExistsException(AlreadyExistsException e) {
        return buildResponse(e);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseBody entityNotFoundException(EntityNotFoundException e) {
        return buildResponse(e);
    }

    @ExceptionHandler({InsufficientFundsException.class, DifferentOwnerException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseBody handleConflict(RuntimeException e) {
        return buildResponse(e);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseBody illegalArguments(IllegalArgumentException e) {
        return buildResponse(e);
    }

    private ErrorResponseBody buildResponse(Exception e) {
        return ErrorResponseBody.builder()
                        .message(e.getMessage())
                        .description(e.getLocalizedMessage())
                        .build();
    }
}
