package com.bank.repository;

import com.bank.entity.Card;
import com.bank.entity.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    boolean existsByCardNumber(String cardNumber);
    List<Card> findAllByOwnerId(Long ownerId);

    List<Card> findByStatusAndExpirationDateBefore(CardStatus cardStatus, LocalDate today);
}
