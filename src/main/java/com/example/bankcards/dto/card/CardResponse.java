package com.example.bankcards.dto.card;

import com.example.bankcards.entity.CardStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardResponse {
    private String cardNumber;
    private String ownerName;
    private String expirationDate;
    private CardStatus status;
    private BigDecimal balance;
}
