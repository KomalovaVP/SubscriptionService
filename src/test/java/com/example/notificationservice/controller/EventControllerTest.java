package com.example.notificationservice.controller;

import com.example.notificationservice.entity.Event;
import com.example.notificationservice.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Test
    void shouldReturnCreatedWhenAddEvent() throws Exception {
        doNothing().when(eventService).createEvent(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\": \"Новый инцидент\", \"eventTime\": \"2025-06-04T15:30:00\"}"))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void shouldReturnOkWhenGetAllEvents() throws Exception {
        doReturn(Collections.emptyList()).when(eventService).findAll();

        mockMvc.perform(MockMvcRequestBuilders.get("/events"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    void shouldReturnEventWhenGetEventById() throws Exception {
        Event event = new Event();
        event.setId(1L);
        event.setMessage("Инцидент");
        event.setEventTime(LocalDateTime.now());

        doReturn(Optional.of(event)).when(eventService).findById(1L);

        mockMvc.perform(MockMvcRequestBuilders.get("/events/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"message\":\"Инцидент\"")));
    }

    @Test
    void shouldReturnNotFoundWhenGetEventByUnknownId() throws Exception {
        doReturn(Optional.empty()).when(eventService).findById(999L);

        mockMvc.perform(MockMvcRequestBuilders.get("/events/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateEvent() throws Exception {
        Event event = new Event();
        event.setId(1L);
        event.setMessage("Обновлённый инцидент");
        event.setEventTime(LocalDateTime.now());

        doNothing().when(eventService).update(event);

        mockMvc.perform(MockMvcRequestBuilders.put("/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\": \"Обновлённый инцидент\", \"eventTime\": \"2025-06-04T15:30:00\"}"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteEvent() throws Exception {
        doNothing().when(eventService).delete(1L); // В реальности сервис не возвращает объект

        mockMvc.perform(MockMvcRequestBuilders.delete("/events/1"))
                .andExpect(status().isNoContent());
    }
}
