package com.example.bankcards.controller;

import com.example.bankcards.dto.auth.*;
import com.example.bankcards.dto.auth.RegisterRequest;
import com.example.bankcards.exception.AlreadyExistsException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.authenticate(loginRequest);
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public String getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return "Username : " + userDetails.getUsername()
                + "Role : " + userDetails.getAuthorities();
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public SimpleResponse register(@RequestBody @Valid RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new AlreadyExistsException(
                    MessageFormat.format("Username: {0} already exists", registerRequest.getUsername())
            );
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AlreadyExistsException(
                    MessageFormat.format("Email: {0} already exists", registerRequest.getEmail())
            );
        }
        authService.register(registerRequest);

        return new SimpleResponse("User registered successfully");
    }

    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public RefreshTokenResponse refreshToken(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/log-out")
    @ResponseStatus(HttpStatus.OK)
    public SimpleResponse logout(@AuthenticationPrincipal UserDetails userDetails) {
        authService.logOut();
        return new SimpleResponse(
                MessageFormat.format(
                        "Logged out successfully, username: {0}",userDetails.getUsername())
        );
    }
}
