package com.example.bankcards.service;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.exception.DifferentOwnerException;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.exception.ExpiredException;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final CardRepository cardRepository;

    @Transactional
    public void transfer(Long userId, Long from, Long to, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        Card fromCard = cardRepository.findById(from).orElseThrow(
                () -> new EntityNotFoundException(
                        MessageFormat.format("Card with ID: {0} not found", from))
        );
        checkBalance(fromCard, amount);
        Card toCard = cardRepository.findById(to).orElseThrow(
                () -> new EntityNotFoundException(
                        MessageFormat.format("Card with ID: {0} not found", to))
        );
        checkStatus(fromCard);
        checkStatus(toCard);

        if (!fromCard.getOwner().getId().equals(userId)) {
            throw new DifferentOwnerException("You are not the owner of the source card");
        }
        if (!toCard.getOwner().getId().equals(userId)) {
            throw new DifferentOwnerException("You cannot transfer to someone else's card");
        }
        fromCard.setBalance(fromCard.getBalance().subtract(amount));
        toCard.setBalance(toCard.getBalance().add(amount));
    }

    public void checkBalance(Card from, BigDecimal amount) {
        BigDecimal balance = from.getBalance();
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(
                    MessageFormat.format("Insufficient funds, balance is: {0}", balance)
            );
        }
    }

    public void checkStatus(Card card) {
        if (card.getStatus() != CardStatus.ACTIVE) {
            if (card.getStatus() == CardStatus.EXPIRED) {
                throw new ExpiredException(
                        MessageFormat.format("Card with ID: {0} is expired", card.getId())
                );
            }
            throw new IllegalStateException(
                    MessageFormat.format("Card with ID: {0} is blocked", card.getId())
            );
        }
    }
}