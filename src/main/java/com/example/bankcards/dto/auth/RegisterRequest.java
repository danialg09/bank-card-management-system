package com.example.bankcards.dto.auth;

import com.example.bankcards.entity.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Username must not be blank")
    private String username;

    @NotBlank(message = "Email must not be blank")
    private String email;

    @NotEmpty(message = "You should write at least one role")
    private Set<RoleType> roles;

    @NotBlank(message = "Password must not be blank")
    private String password;
}
