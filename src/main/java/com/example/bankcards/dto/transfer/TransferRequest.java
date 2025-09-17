package com.example.bankcards.dto.transfer;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferRequest {

    @NotNull(message = "Source card ID must not be null")
    private Long fromCardId;

    @NotNull(message = "Target card ID must not be null")
    private Long toCardId;

    @DecimalMin(value = "0.0", inclusive = false, message = "Transfer amount must be greater than 0")
    private BigDecimal amount;
}
