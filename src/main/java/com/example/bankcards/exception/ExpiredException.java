package com.example.bankcards.exception;

public class ExpiredException extends RuntimeException {
    public ExpiredException(String message) {
        super(message);
    }
}
