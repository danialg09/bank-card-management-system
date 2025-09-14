package com.example.bankcards.controller;

import com.example.bankcards.dto.auth.*;
import com.example.bankcards.dto.user.UserRequest;
import com.example.bankcards.exception.AlreadyExistsException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final SecurityService securityService;

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> authUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(securityService.authenticate(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<SimpleResponse> registerUser(@RequestBody UserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new AlreadyExistsException("Username already exists");
        }

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new AlreadyExistsException("Email already exists");
        }
        securityService.register(userRequest);

        return ResponseEntity.ok(new SimpleResponse("User registered successfully"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(securityService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/log-out")
    public ResponseEntity<SimpleResponse> logout(@AuthenticationPrincipal UserDetails userDetails) {
        securityService.logOut();
        return ResponseEntity.ok(new SimpleResponse("Logged out successfully, username: " + userDetails.getUsername()));
    }
}
