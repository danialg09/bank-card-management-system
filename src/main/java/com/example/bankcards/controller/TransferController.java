package com.example.bankcards.controller;

import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.security.AppUserDetails;
import com.example.bankcards.service.TransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transfer")
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    public void transferMoney(@RequestBody @Valid TransferRequest request,
                              @AuthenticationPrincipal AppUserDetails userDetails) {
        transferService.transfer(userDetails.getId(), request);
    }
}
