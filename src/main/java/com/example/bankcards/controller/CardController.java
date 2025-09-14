package com.example.bankcards.controller;

import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.dto.card.CardStatusRequest;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CardResponse findById(@PathVariable Long id) {
        return mapper.toCardResponse(service.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CardResponse create(@RequestParam Long ownerId) {
        return mapper.toCardResponse(service.create(ownerId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CardResponse updateStatus(@PathVariable Long id, @RequestBody CardStatusRequest status) {
        return mapper.toCardResponse(service.update(id, status.getStatus()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(@PathVariable Long id) {
        service.delete(id);
    }
}
