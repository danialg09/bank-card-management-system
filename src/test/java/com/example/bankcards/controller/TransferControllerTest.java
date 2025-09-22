package com.example.bankcards.controller;

import com.example.bankcards.AbstractControllerTest;
import com.example.bankcards.StringTestUtils;
import com.example.bankcards.dto.transfer.TransferRequest;
import com.example.bankcards.entity.RoleType;
import com.example.bankcards.entity.User;
import com.example.bankcards.security.AppUserDetails;
import com.example.bankcards.service.TransferService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransferController.class)
public class TransferControllerTest extends AbstractControllerTest {

    @MockitoBean
    private TransferService service;

    @Test
    public void testTransfer() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("pass");
        user.setRoles(Set.of(RoleType.ROLE_USER));

        AppUserDetails userDetails = new AppUserDetails(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setFromCardId(1L);
        transferRequest.setToCardId(2L);
        transferRequest.setAmount(BigDecimal.TEN);

        Mockito.doNothing().when(service).transfer(
                Mockito.eq(transferRequest.getFromCardId()),
                Mockito.eq(transferRequest)
        );

        String jsonRequest = StringTestUtils.toJson(transferRequest);

        mockMvc.perform(post("/api/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .with(csrf()))
                .andExpect(status().isOk());

        Mockito.verify(service, Mockito.times(1))
                .transfer(
                        Mockito.eq(transferRequest.getFromCardId()),
                        Mockito.eq(transferRequest)
                );
    }
}
