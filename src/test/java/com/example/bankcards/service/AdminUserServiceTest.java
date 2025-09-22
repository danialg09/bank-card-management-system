package com.example.bankcards.service;

import com.example.bankcards.entity.User;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AdminUserServiceTest {
    private final UserRepository repository = mock(UserRepository.class);
    private final AdminUserService service = new AdminUserService(repository);

    @Test
    void findAllShouldReturnUsers() {
        User user1 = new User();
        User user2 = new User();
        when(repository.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = service.findAll();

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void findByIdShouldReturnUser() {
        User user = new User();
        user.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        User result = service.findById(1L);

        assertEquals(1L, result.getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void findByIdShouldThrowIfNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void findByUsernameShouldReturnUser() {
        User user = new User();
        user.setUsername("test");
        when(repository.findByUsername("test")).thenReturn(Optional.of(user));

        User result = service.findByUsername("test");

        assertEquals("test", result.getUsername());
        verify(repository, times(1)).findByUsername("test");
    }

    @Test
    void findByUsernameShouldThrowIfNotFound() {
        when(repository.findByUsername("test")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findByUsername("test"));
    }

    @Test
    void deleteShouldCallRepository() {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}
