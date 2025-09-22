package com.example.bankcards.service;

import com.example.bankcards.dto.card.CardRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.DifferentOwnerException;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CardServiceTest {

    private final CardRepository cardRepository = mock(CardRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);

    private final CardService cardService = new CardService(cardRepository, userRepository);

    @Test
    void findAll_shouldReturnAllCards() {
        Card card1 = new Card();
        Card card2 = new Card();
        when(cardRepository.findAll()).thenReturn(List.of(card1, card2));

        List<Card> result = cardService.findAll();

        assertEquals(2, result.size());
        verify(cardRepository).findAll();
    }

    @Test
    void findById_existingId_shouldReturnCard() {
        Card card = new Card();
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        Card result = cardService.findById(1L);

        assertNotNull(result);
        verify(cardRepository).findById(1L);
    }

    @Test
    void findById_nonExistingId_shouldThrowException() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> cardService.findById(1L));
    }

    @Test
    void create_shouldSaveCard() {
        User user = new User();
        user.setId(1L);

        CardRequest request = new CardRequest();
        request.setOwnerId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(cardRepository.existsByCardNumber(anyString())).thenReturn(false);
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Card result = cardService.create(request);

        assertNotNull(result);
        assertEquals(user, result.getOwner());
        assertEquals(CardStatus.ACTIVE, result.getStatus());
        assertNotNull(result.getCardNumber());
        verify(cardRepository).save(any(Card.class));
    }

    @Test
    void getBalance_ownerMatches_shouldReturnCard() {
        User user = new User();
        user.setId(1L);
        Card card = new Card();
        card.setOwner(user);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        Card result = cardService.getBalance(1L, 1L);

        assertEquals(card, result);
    }

    @Test
    void getBalance_ownerDoesNotMatch_shouldThrowException() {
        User user = new User();
        user.setId(2L);
        Card card = new Card();
        card.setOwner(user);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        assertThrows(DifferentOwnerException.class, () -> cardService.getBalance(1L, 1L));
    }

    @Test
    void getCards_shouldReturnUserCards() {
        when(cardRepository.findAllByOwnerId(1L)).thenReturn(List.of(new Card()));

        List<Card> result = cardService.getCards(1L);

        assertEquals(1, result.size());
        verify(cardRepository).findAllByOwnerId(1L);
    }

    @Test
    void update_shouldChangeStatus() {
        Card card = new Card();
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(card)).thenReturn(card);

        Card result = cardService.update(1L, CardStatus.BLOCKED);

        assertEquals(CardStatus.BLOCKED, result.getStatus());
        verify(cardRepository).save(card);
    }

    @Test
    void delete_shouldCallRepository() {
        doNothing().when(cardRepository).deleteById(1L);

        cardService.delete(1L);

        verify(cardRepository).deleteById(1L);
    }
}
