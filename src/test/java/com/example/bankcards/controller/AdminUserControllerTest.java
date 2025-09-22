package com.example.bankcards.controller;

import com.example.bankcards.AbstractControllerTest;
import com.example.bankcards.dto.user.UserResponse;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.UserMapper;
import com.example.bankcards.service.AdminUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdminUserController.class)
class AdminUserControllerTest extends AbstractControllerTest {

    @MockitoBean
    private AdminUserService service;

    @MockitoBean
    private UserMapper mapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminShouldGetAllUsers() throws Exception {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername("admin");

        List<UserResponse> users = List.of(userResponse);
        List<User> userEntities = List.of(new User());

        Mockito.when(service.findAll()).thenReturn(userEntities);
        Mockito.when(mapper.usersToUserResponses(userEntities)).thenReturn(users);

        mockMvc.perform(get("/api/admin/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(service, times(1)).findAll();
        Mockito.verify(mapper, times(1)).usersToUserResponses(userEntities);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminShouldGetUserById() throws Exception {
        Long userId = 1L;

        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setUsername("admin");

        Mockito.when(service.findById(userId)).thenReturn(new User());
        Mockito.when(mapper.userToUserResponse(any(User.class))).thenReturn(userResponse);

        mockMvc.perform(get("/api/admin/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(service, times(1)).findById(userId);
        Mockito.verify(mapper, times(1)).userToUserResponse(any(User.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminShouldGetUserByUsername() throws Exception {
        String username = "admin";

        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername(username);

        Mockito.when(service.findByUsername(username)).thenReturn(new User());
        Mockito.when(mapper.userToUserResponse(any(User.class))).thenReturn(userResponse);

        mockMvc.perform(get("/api/admin/users/by-name/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(service, times(1)).findByUsername(username);
        Mockito.verify(mapper, times(1)).userToUserResponse(any(User.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminShouldDeleteUserById() throws Exception {
        Long userId = 1L;

        Mockito.doNothing().when(service).delete(userId);

        mockMvc.perform(delete("/api/admin/users/{id}", userId)
                        .with(csrf()))
                .andExpect(status().isOk());

        Mockito.verify(service, times(1)).delete(userId);
    }
}
