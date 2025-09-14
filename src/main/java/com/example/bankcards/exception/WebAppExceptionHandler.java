package com.example.bankcards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class WebAppExceptionHandler {

    @ExceptionHandler(value = RefreshTokenException.class)
    public ResponseEntity<ErrorResponseBody> refreshTokenException(RefreshTokenException e, WebRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, e, request);
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    public ResponseEntity<ErrorResponseBody> alreadyExistsException(AlreadyExistsException e, WebRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, e, request);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseBody> entityNotFoundException(EntityNotFoundException e, WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, e, request);
    }

    private ResponseEntity<ErrorResponseBody> buildResponse(HttpStatus httpStatus, Exception e, WebRequest request) {
        return ResponseEntity.status(httpStatus)
                .body(ErrorResponseBody.builder()
                        .message(e.getMessage())
                        .description(request.getDescription(false))
                        .build());
    }


}
