package com.bank.controller;

import com.bank.dto.card.CardRequest;
import com.bank.dto.card.CardResponse;
import com.bank.dto.card.CardStatusRequest;
import com.bank.mapper.CardMapper;
import com.bank.security.AppUserDetails;
import com.bank.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cards")
public class CardController {

    private final CardService service;
    private final CardMapper mapper;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CardResponse> findAll() {
        return mapper.toCardResponses(service.findAll());
    }

    @GetMapping("/my-cards")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<CardResponse> myCards(@AuthenticationPrincipal AppUserDetails userDetails) {
        return mapper.toCardResponses(service.getCards(userDetails.getId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CardResponse findById(@PathVariable Long id) {
        return mapper.toCardResponse(service.findById(id));
    }

    @GetMapping("/balance/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public CardResponse balance(@PathVariable Long id, @AuthenticationPrincipal AppUserDetails userDetails) {
        return mapper.toCardResponse(service.getBalance(id, userDetails.getId()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CardResponse create(@RequestBody @Valid CardRequest request) {
        return mapper.toCardResponse(service.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CardResponse updateStatus(@PathVariable Long id, @RequestBody @Valid CardStatusRequest status) {
        return mapper.toCardResponse(service.update(id, status.getStatus()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(@PathVariable Long id) {
        service.delete(id);
    }
}
