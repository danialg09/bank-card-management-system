package com.example.bankcards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class WebAppExceptionHandler {

    @ExceptionHandler(value = RefreshTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseBody refreshTokenException(RefreshTokenException e, WebRequest request) {
        return buildResponse(e, request);
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseBody alreadyExistsException(AlreadyExistsException e, WebRequest request) {
        return buildResponse(e, request);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseBody entityNotFoundException(EntityNotFoundException e, WebRequest request) {
        return buildResponse(e, request);
    }

    @ExceptionHandler({InsufficientFundsException.class, DifferentOwnerException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseBody handleConflict(RuntimeException e, WebRequest request) {
        return buildResponse(e, request);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseBody illegalArguments(IllegalArgumentException e, WebRequest request) {
        return buildResponse(e, request);
    }

    private ErrorResponseBody buildResponse(Exception e, WebRequest request) {
        return ErrorResponseBody.builder()
                        .message(e.getMessage())
                        .description(request.getDescription(false))
                        .build();
    }
}
