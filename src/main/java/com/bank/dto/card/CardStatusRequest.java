package com.bank.dto.card;

import com.bank.entity.CardStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardStatusRequest {

    @NotNull(message = "Card status must not be blank")
    private CardStatus status;
}
