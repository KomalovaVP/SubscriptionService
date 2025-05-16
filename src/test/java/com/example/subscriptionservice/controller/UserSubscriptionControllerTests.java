package com.example.subscriptionservice.controller;

import com.example.subscriptionservice.model.Subscription;
import com.example.subscriptionservice.model.User;
import com.example.subscriptionservice.service.SubscriptionService;
import com.example.subscriptionservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserSubscriptionController.class)
@ContextConfiguration(classes = {UserSubscriptionController.class})
class UserSubscriptionControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private SubscriptionService subscriptionService;

    // Тестирование метода создания пользователя
    @Test
    void testCreateUser() throws Exception {
        User newUser = new User(); // Здесь инициализируйте объект User с необходимыми полями

        when(userService.createUser(newUser)).thenReturn(newUser); // Возвращаем тот же самый созданный объект

        mvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newUser)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/users/")));
    }

    // Тестирование метода получения пользователя по ID
    @Test
    void testGetUserById() throws Exception {
        long userId = 1L;
        User existingUser = new User(); // Заполните существующего пользователя

        when(userService.getUserById(userId)).thenReturn(existingUser);

        mvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
    }

    // Тест обновления пользователя
    @Test
    void testUpdateUser() throws Exception {
        long userId = 1L;
        User updatedUserData = new User(); // Подготовьте обновленные данные пользователя

        when(userService.updateUser(userId, updatedUserData)).thenReturn(updatedUserData);

        mvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedUserData)))
                .andExpect(status().isOk())
    }

    // Тест удаления пользователя
    @Test
    void testDeleteUser() throws Exception {
        long userId = 1L;

        mvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isNoContent());
    }

    // Тест добавления подписки пользователю
    @Test
    void testAddSubscriptionToUser() throws Exception {
        long userId = 1L;
        Subscription newSubscription = new Subscription(); // Создаем новую подписку

        when(subscriptionService.addSubscription(userId, newSubscription)).thenReturn(newSubscription);

        mvc.perform(post("/users/{userId}/subscriptions", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newSubscription)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/users/" + userId + "/subscriptions/")));
    }

    // Тест получения подписок пользователя
    @Test
    void testGetSubscriptionsForUser() throws Exception {
        long userId = 1L;
        List<Subscription> subscriptionsList = Collections.singletonList(new Subscription()); // Создаем список подписок

        when(subscriptionService.getSubscriptionsForUser(userId)).thenReturn(subscriptionsList);

        mvc.perform(get("/users/{userId}/subscriptions", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1)))); // Убираем ненужное приведение ResultMatcher
    }

    // Тест удаления подписки пользователя
    @Test
    void testDeleteSubscriptionFromUser() throws Exception {
        long userId = 1L;
        long subId = 1L;

        mvc.perform(delete("/users/{userId}/subscriptions/{subId}", userId, subId))
                .andExpect(status().isNoContent());
    }

    // Вспомогательная функция для сериализации объектов в JSON
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
