package com.example.bankcards.exception;

public class DifferentOwnerException extends RuntimeException {
    public DifferentOwnerException(String message) {
        super(message);
    }
}
