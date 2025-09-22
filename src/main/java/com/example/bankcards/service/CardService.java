package com.example.bankcards.service;

import com.example.bankcards.dto.card.CardRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.DifferentOwnerException;
import com.example.bankcards.exception.EntityNotFoundException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public List<Card> findAll() {
        return cardRepository.findAll();
    }

    public Card findById(Long id) {
        return cardRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        MessageFormat.format("Card with ID: {0} not found", id))
        );
    }

    @Scheduled(cron = "0 0 0/4 * * ?")
    public void expireCards() {
        LocalDate today = LocalDate.now();
        List<Card> expiredCards = cardRepository
                .findByStatusAndExpirationDateBefore(CardStatus.ACTIVE, today);

        for (Card card : expiredCards) {
            card.setStatus(CardStatus.EXPIRED);
        }
        cardRepository.saveAll(expiredCards);
    }

    public Card create(CardRequest request) {
        User owner = userRepository.findById(request.getOwnerId()).orElseThrow(
                () -> new EntityNotFoundException(
                        MessageFormat.format("User with ID: {0} not found", request.getOwnerId()))
        );
        Card card = new Card();
        String cardNumber;
        do {
            cardNumber = CardUtils.generateCardNumber();
        } while (cardRepository.existsByCardNumber(cardNumber));

        card.setOwner(owner);
        card.setStatus(CardStatus.ACTIVE);
        card.setExpirationDate(LocalDate.now().plusYears(5));
        card.setBalance(BigDecimal.ZERO);
        card.setCardNumber(cardNumber);
        return cardRepository.save(card);
    }

    public Card getBalance(Long cardId, Long userId) {
        Card card = findById(cardId);
        if (card.getOwner().getId().equals(userId)) {
            return card;
        } else {
            throw new DifferentOwnerException(
                    MessageFormat.format("You are not owner of card with ID: {0}", cardId));
        }
    }

    public List<Card> getCards(Long userId) {
        return cardRepository.findAllByOwnerId(userId);
    }

    @Transactional
    public Card update(Long id, CardStatus status) {
        Card exist = findById(id);
        exist.setStatus(status);
        return cardRepository.save(exist);
    }

    @Transactional
    public void delete(Long id) {
        cardRepository.deleteById(id);
    }
}
