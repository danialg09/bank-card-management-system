package com.example.bankcards.service;

import com.example.bankcards.dto.transfer.TransferRequest;
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
    public void transfer(Long userId, TransferRequest request) {
        Card fromCard = cardRepository.findById(request.getFromCardId()).orElseThrow(
                () -> new EntityNotFoundException(
                        MessageFormat.format("Card with ID: {0} not found", request.getFromCardId()))
        );
        checkBalance(fromCard, request.getAmount());
        Card toCard = cardRepository.findById(request.getToCardId()).orElseThrow(
                () -> new EntityNotFoundException(
                        MessageFormat.format("Card with ID: {0} not found", request.getToCardId()))
        );
        checkStatus(fromCard);
        checkStatus(toCard);

        if (!fromCard.getOwner().getId().equals(userId)) {
            throw new DifferentOwnerException("You are not the owner of the source card");
        }
        if (!toCard.getOwner().getId().equals(userId)) {
            throw new DifferentOwnerException("You cannot transfer to someone else's card");
        }
        fromCard.setBalance(fromCard.getBalance().subtract(request.getAmount()));
        toCard.setBalance(toCard.getBalance().add(request.getAmount()));
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