package com.bank.event;

public record UserRegisteredEvent(
        Long userId,
        String email,
        String role
) {}
