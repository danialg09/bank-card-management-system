package com.example.bankcards.controller;

import com.example.bankcards.dto.auth.*;
import com.example.bankcards.dto.auth.RegisterRequest;
import com.example.bankcards.exception.AlreadyExistsException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.authenticate(loginRequest));
    }

    @GetMapping("/me")
    public ResponseEntity<String> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok("Username : " + userDetails.getUsername() + "Role : " + userDetails.getAuthorities());
    }

    @PostMapping("/register")
    public ResponseEntity<SimpleResponse> register(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new AlreadyExistsException("Username already exists");
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new AlreadyExistsException("Email already exists");
        }
        authService.register(registerRequest);

        return ResponseEntity.ok(new SimpleResponse("User registered successfully"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/log-out")
    public ResponseEntity<SimpleResponse> logout(@AuthenticationPrincipal UserDetails userDetails) {
        authService.logOut();
        return ResponseEntity.ok(new SimpleResponse("Logged out successfully, username: " + userDetails.getUsername()));
    }
}
