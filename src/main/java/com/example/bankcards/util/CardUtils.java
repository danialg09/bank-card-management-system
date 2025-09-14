package com.example.bankcards.util;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class CardUtils {

    public String generateCardNumber() {
        Random random = new Random();
        return String.format("%016d", Math.abs(random.nextLong()) % 1_0000_0000_0000_0000L);
    }

    public String getMaskedCardNumber(String cardNumber) {
        return "**** **** **** " + cardNumber.substring(12);
    }
}
