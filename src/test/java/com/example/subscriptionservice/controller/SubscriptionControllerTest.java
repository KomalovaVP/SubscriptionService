package com.example.subscriptionservice.controller;

import com.example.subscriptionservice.service.SubscriptionService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubscriptionController.class)
class SubscriptionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SubscriptionService subscriptionService;

    @BeforeEach
    void setUp() {
        given(subscriptionService.getTopThreePopularSubscriptions()).willReturn(Arrays.asList("Subscription A", "Subscription B", "Subscription C"));
    }

    @Test
    void shouldReturnTop3PopularSubscriptions() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/subscriptions/top"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3))) // Проверяем длину массива
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]", Matchers.is("Subscription A"))) // Проверяем первый элемент
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]", Matchers.is("Subscription B")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2]", Matchers.is("Subscription C")));
    }
}
