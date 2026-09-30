package com.bank.event;

public record UserRegisteredEvent(
        Long userId,
        String username,
        String email
) {}
