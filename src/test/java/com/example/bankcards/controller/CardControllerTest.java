package com.example.bankcards.controller;

import com.example.bankcards.AbstractControllerTest;
import com.example.bankcards.StringTestUtils;
import com.example.bankcards.dto.card.CardRequest;
import com.example.bankcards.dto.card.CardStatusRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.RoleType;
import com.example.bankcards.entity.User;
import com.example.bankcards.mapper.CardMapper;
import com.example.bankcards.security.AppUserDetails;
import com.example.bankcards.service.CardService;
import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import com.example.bankcards.dto.card.CardResponse;
import com.example.bankcards.entity.CardStatus;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CardController.class)
class CardControllerTest extends AbstractControllerTest {

    @MockitoBean
    private CardService cardService;

    @MockitoBean
    private CardMapper cardMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminShouldGetAllCards() throws Exception {
        System.out.println(cardService.getClass());
        CardResponse cardResponse = CardResponse.builder().id(1L)
                .cardNumber("1234")
                .balance(BigDecimal.valueOf(777.00))
                .status(CardStatus.ACTIVE).build();

        CardResponse cardResponse2 = CardResponse.builder().id(2L)
                .cardNumber("4321").balance(BigDecimal.valueOf(10.00))
                .status(CardStatus.BLOCKED).build();

        List<CardResponse> cards = List.of(cardResponse, cardResponse2);
        List<Card> list = List.of(
                Card.builder().id(1L).cardNumber("1234").balance(BigDecimal.valueOf(777.00)).build(),
                Card.builder().id(2L).cardNumber("4321").balance(BigDecimal.valueOf(10.00)).build());

        when(cardMapper.toCardResponses(anyList())).thenReturn(cards);
        when(cardService.findAll()).thenReturn(list);

        String actual = mockMvc.perform(get("/api/cards"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(cardService, Mockito.times(1)).findAll();
        Mockito.verify(cardMapper, Mockito.times(1)).toCardResponses(anyList());

        String expected = StringTestUtils
                .readStringFromResource("responses/card/findAllCards_response.json");

        JsonAssert.assertJsonEquals(expected, actual);
    }

    @Test
    void userShouldGetMyCards() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("pass");
        user.setRoles(Set.of(RoleType.ROLE_USER));

        AppUserDetails userDetails = new AppUserDetails(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        CardResponse cardResponse = CardResponse.builder()
                .id(1L)
                .ownerName(userDetails.getUsername())
                .cardNumber("1234")
                .balance(BigDecimal.valueOf(777.0))
                .status(CardStatus.ACTIVE)
                .build();

        Mockito.when(cardService.getCards(userDetails.getId()))
                .thenReturn(List.of(new Card()));
        Mockito.when(cardMapper.toCardResponses(Mockito.anyList()))
                .thenReturn(List.of(cardResponse));

        String actual = mockMvc.perform(get("/api/cards/my-cards"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(cardService, Mockito.times(1)).getCards(userDetails.getId());
        Mockito.verify(cardMapper, Mockito.times(1)).toCardResponses(Mockito.anyList());

        String expected = StringTestUtils.readStringFromResource("responses/card/myCards_response.json");
        JsonAssert.assertJsonEquals(expected, actual);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminShouldFindCardById() throws Exception {
        Long cardId = 1L;

        CardResponse cardResponse = CardResponse.builder()
                .id(cardId)
                .cardNumber("1234")
                .balance(BigDecimal.valueOf(777.0))
                .status(CardStatus.ACTIVE)
                .build();

        Mockito.when(cardService.findById(cardId)).thenReturn(new Card());
        Mockito.when(cardMapper.toCardResponse(Mockito.any(Card.class))).thenReturn(cardResponse);

        String actual = mockMvc.perform(get("/api/cards/{id}", cardId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(cardService, Mockito.times(1)).findById(cardId);
        Mockito.verify(cardMapper, Mockito.times(1)).toCardResponse(Mockito.any(Card.class));

        String expected = StringTestUtils.readStringFromResource("responses/card/findById_response.json");
        JsonAssert.assertJsonEquals(expected, actual);
    }

    @Test
    void userShouldGetBalance() throws Exception {
        Long cardId = 1L;

        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("pass");
        user.setRoles(Set.of(RoleType.ROLE_USER));

        AppUserDetails userDetails = new AppUserDetails(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        CardResponse cardResponse = CardResponse.builder()
                .id(cardId)
                .ownerName(userDetails.getUsername())
                .cardNumber("1234")
                .balance(BigDecimal.valueOf(777.0))
                .status(CardStatus.ACTIVE)
                .build();

        Mockito.when(cardService.getBalance(cardId, userDetails.getId())).thenReturn(new Card());
        Mockito.when(cardMapper.toCardResponse(Mockito.any(Card.class))).thenReturn(cardResponse);

        String actual = mockMvc.perform(get("/api/cards/balance/{id}", cardId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(cardService, Mockito.times(1)).getBalance(cardId, userDetails.getId());
        Mockito.verify(cardMapper, Mockito.times(1)).toCardResponse(Mockito.any(Card.class));

        String expected = StringTestUtils.readStringFromResource("responses/card/balance_response.json");
        JsonAssert.assertJsonEquals(expected, actual);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminShouldCreateCard() throws Exception {
        CardRequest request = new CardRequest();
        request.setOwnerId(1L);
        request.setBalance(BigDecimal.valueOf(777.0));

        CardResponse cardResponse = CardResponse.builder()
                .id(1L)
                .balance(BigDecimal.valueOf(777.0))
                .status(CardStatus.ACTIVE)
                .build();

        Mockito.when(cardService.create(Mockito.any(CardRequest.class))).thenReturn(new Card());
        Mockito.when(cardMapper.toCardResponse(Mockito.any(Card.class))).thenReturn(cardResponse);

        String jsonRequest = StringTestUtils.toJson(request);

        String actual = mockMvc.perform(post("/api/cards")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(cardService, Mockito.times(1)).create(Mockito.any(CardRequest.class));
        Mockito.verify(cardMapper, Mockito.times(1)).toCardResponse(Mockito.any(Card.class));

        String expected = StringTestUtils.readStringFromResource("responses/card/createCard_response.json");

        JsonAssert.assertJsonEquals(expected, actual, JsonAssert.whenIgnoringPaths("cardNumber"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminShouldUpdateStatus() throws Exception {
        Long cardId = 1L;
        CardStatusRequest statusRequest = new CardStatusRequest();
        statusRequest.setStatus(CardStatus.BLOCKED);

        CardResponse cardResponse = CardResponse.builder()
                .id(cardId)
                .cardNumber("1234")
                .balance(BigDecimal.valueOf(777.0))
                .status(CardStatus.BLOCKED)
                .build();

        Mockito.when(cardService.update(Mockito.eq(cardId), Mockito.eq(CardStatus.BLOCKED))).thenReturn(new Card());
        Mockito.when(cardMapper.toCardResponse(Mockito.any(Card.class))).thenReturn(cardResponse);

        String jsonRequest = StringTestUtils.toJson(statusRequest);

        String actual = mockMvc.perform(put("/api/cards/{id}", cardId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Mockito.verify(cardService, Mockito.times(1)).update(Mockito.eq(cardId), Mockito.eq(CardStatus.BLOCKED));
        Mockito.verify(cardMapper, Mockito.times(1)).toCardResponse(Mockito.any(Card.class));

        String expected = StringTestUtils.readStringFromResource("responses/card/updateStatus_response.json");
        JsonAssert.assertJsonEquals(expected, actual);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminShouldDeleteCard() throws Exception {
        Long cardId = 1L;

        Mockito.doNothing().when(cardService).delete(cardId);

        mockMvc.perform(delete("/api/cards/{id}", cardId)
                        .with(csrf()))
                .andExpect(status().isOk());

        Mockito.verify(cardService, Mockito.times(1)).delete(cardId);
    }
}

