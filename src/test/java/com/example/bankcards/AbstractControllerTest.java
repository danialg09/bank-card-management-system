package com.example.bankcards;

import com.example.bankcards.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected JwtUtils jwtUtils;
}
