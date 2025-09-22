package com.example.bankcards.dto.card;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CardRequest {

    @NotNull(message = "Owner ID must not be null")
    private Long ownerId;

    @DecimalMin(value = "0.0", message = "Balance must be greater than or equal to 0")
    private BigDecimal balance = BigDecimal.ZERO;

    private LocalDate expiryDate = LocalDate.now().plusYears(5);
}
