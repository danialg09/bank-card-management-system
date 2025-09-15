package com.example.bankcards.controller;

import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final AdminUserService service;
    private final UserMapper mapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> findAll() {
        return mapper.usersToUserResponses(service.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse findById(@PathVariable Long id) {
        return mapper.userToUserResponse(service.findById(id));
    }

    @GetMapping("/by-name/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse findByUsername(@PathVariable String username) {
        return mapper.userToUserResponse(service.findByUsername(username));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(@PathVariable Long id) {
        service.delete(id);
    }
}
