package com.example.bankcards.security;

import com.example.bankcards.dto.auth.AuthResponse;
import com.example.bankcards.dto.auth.LoginRequest;
import com.example.bankcards.dto.auth.RefreshTokenRequest;
import com.example.bankcards.dto.auth.RefreshTokenResponse;
import com.example.bankcards.dto.user.UserRequest;
import com.example.bankcards.entity.RefreshToken;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.RefreshTokenException;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.jwt.JwtUtils;
import com.example.bankcards.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RefreshTokenService refreshTokenService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthResponse authenticate(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        RefreshToken refreshToken = refreshTokenService.save(userDetails.getId());

        return AuthResponse.builder()
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .token(jwtUtils.generateToken(userDetails))
                .refreshToken(refreshToken.getToken())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
    }

    public void register(UserRequest request) {
       var user = User.builder()
               .username(request.getUsername())
               .email(request.getEmail())
               .password(passwordEncoder.encode(request.getPassword()))
               .build();

       user.setRoles(request.getRoles());
       userRepository.save(user);
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        return refreshTokenService.findByRefreshToken(refreshToken)
                .map(refreshTokenService::checkRefreshToken)
                .map(RefreshToken::getUserId)
                .map(userId -> {
                    User tokenUser = userRepository.findById(userId).orElseThrow(
                            () -> new RefreshTokenException("Exception of trying to get refresh token from userId: " + userId));
                    String token = jwtUtils.generateTokenFromUsername(tokenUser.getUsername());

                    return new RefreshTokenResponse(token, refreshTokenService.save(userId).getToken());
                }).orElseThrow(() -> new RefreshTokenException(refreshToken, "Refresh token not found"));
    }

    public void logOut() {
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails) {
            Long userId = userDetails.getId();

            refreshTokenService.deleteByUserId(userId);
        }
    }

}
