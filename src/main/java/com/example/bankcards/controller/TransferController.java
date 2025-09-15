package com.example.bankcards.controller;

import com.example.bankcards.security.AppUserDetails;
import com.example.bankcards.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transfer")
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public void transferMoney(@RequestParam Long from, @RequestParam Long to, @RequestParam BigDecimal amount,
                              @AuthenticationPrincipal AppUserDetails userDetails) {
        transferService.transfer(userDetails.getId(), from, to, amount);
    }
}
