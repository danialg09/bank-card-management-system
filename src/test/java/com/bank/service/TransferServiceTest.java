package com.bank.service;

import com.bank.dto.transfer.TransferRequest;
import com.bank.entity.Card;
import com.bank.entity.CardStatus;
import com.bank.entity.User;
import com.bank.exception.DifferentOwnerException;
import com.bank.exception.InsufficientFundsException;
import com.bank.repository.CardRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransferServiceTest {

    private final CardRepository cardRepository = mock(CardRepository.class);
    private final TransferService transferService = new TransferService(cardRepository);

    @Test
    void transferShouldWork() {
        User user = new User();
        user.setId(1L);

        Card fromCard = new Card();
        fromCard.setId(1L);
        fromCard.setOwner(user);
        fromCard.setBalance(BigDecimal.valueOf(100));
        fromCard.setStatus(CardStatus.ACTIVE);

        Card toCard = new Card();
        toCard.setId(2L);
        toCard.setOwner(user);
        toCard.setBalance(BigDecimal.valueOf(50));
        toCard.setStatus(CardStatus.ACTIVE);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));

        TransferRequest request = new TransferRequest();
        request.setFromCardId(1L);
        request.setToCardId(2L);
        request.setAmount(BigDecimal.valueOf(30));

        transferService.transfer(1L, request);

        assertEquals(BigDecimal.valueOf(70), fromCard.getBalance());
        assertEquals(BigDecimal.valueOf(80), toCard.getBalance());
    }

    @Test
    void transferShouldThrowIfInsufficientFunds() {
        User user = new User();
        user.setId(1L);

        Card fromCard = new Card();
        fromCard.setId(1L);
        fromCard.setOwner(user);
        fromCard.setBalance(BigDecimal.valueOf(10));
        fromCard.setStatus(CardStatus.ACTIVE);

        Card toCard = new Card();
        toCard.setId(2L);
        toCard.setOwner(user);
        toCard.setBalance(BigDecimal.valueOf(50));
        toCard.setStatus(CardStatus.ACTIVE);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));

        TransferRequest request = new TransferRequest();
        request.setFromCardId(1L);
        request.setToCardId(2L);
        request.setAmount(BigDecimal.valueOf(30));

        assertThrows(InsufficientFundsException.class, () ->
                transferService.transfer(1L, request));
    }

    @Test
    void transferShouldThrowIfDifferentOwner() {
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        Card fromCard = new Card();
        fromCard.setId(1L);
        fromCard.setOwner(user1);
        fromCard.setBalance(BigDecimal.valueOf(100));
        fromCard.setStatus(CardStatus.ACTIVE);

        Card toCard = new Card();
        toCard.setId(2L);
        toCard.setOwner(user2); // другой владелец
        toCard.setBalance(BigDecimal.valueOf(50));
        toCard.setStatus(CardStatus.ACTIVE);

        when(cardRepository.findById(1L)).thenReturn(Optional.of(fromCard));
        when(cardRepository.findById(2L)).thenReturn(Optional.of(toCard));

        TransferRequest request = new TransferRequest();
        request.setFromCardId(1L);
        request.setToCardId(2L);
        request.setAmount(BigDecimal.valueOf(30));

        assertThrows(DifferentOwnerException.class, () ->
                transferService.transfer(1L, request));
    }
}
