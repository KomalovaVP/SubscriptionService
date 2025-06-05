package com.example.notificationservice.controller;

import com.example.notificationservice.entity.User;
import com.example.notificationservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setId(1L);
        user.setFullName("John Doe");
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        doNothing().when(userService).create(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\": \"Jane Smith\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetUserByIdExists() throws Exception {
        doReturn(Optional.of(user)).when(userService).findById(1L);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"fullName\":\"John Doe\"")));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        doReturn(Optional.empty()).when(userService).findById(999L);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        doNothing().when(userService).update(user);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\": \"Updated Name\"}"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}
