package com.example.notificationservice.service;

import com.example.notificationservice.dao.EventDao;
import com.example.notificationservice.entity.Event;
import com.example.notificationservice.entity.NotificationInterval;
import com.example.notificationservice.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @InjectMocks
    private EventService eventService;

    @Mock
    private EventDao eventDao;

    @Mock
    private UserService userService;

    @Mock
    private TaskScheduler taskScheduler;

    @Mock
    private SimpMessagingTemplate template;

    private Event event;
    private User user;

    @BeforeEach
    public void setup() {
        event = new Event();
        event.setId(1L);
        event.setMessage("Test Message");
        event.setEventTime(LocalDateTime.now());

        user = new User();
        user.setId(1L);
        user.setFullName("John Doe");
    }

    @Test
    void testFindAllEvents() {
        when(eventDao.findAll()).thenReturn(Collections.singletonList(event));

        Iterable<Event> result = eventService.findAll();

        assertEquals(1, ((Iterable<?>)result).spliterator().estimateSize());
    }

    @Test
    void testFindById() {
        when(eventDao.findById(1L)).thenReturn(Optional.of(event));

        Optional<Event> result = eventService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(event, result.get());
    }

    @Test
    void testUpdateEvent() {
        when(eventDao.save(any(Event.class))).thenReturn(event);

        eventService.update(event);

        verify(eventDao).save(event);
    }

    @Test
    void testDeleteEvent() {
        eventService.delete(1L);

        verify(eventDao).deleteById(1L);
    }

    @Test
    void testCreateEvent() {
        when(eventDao.save(any(Event.class))).thenReturn(event);
        when(userService.findAll()).thenReturn(Collections.singletonList(user));

        eventService.createEvent(event);

        verify(eventDao).save(event);
    }

    @Test
    void testSendNotificationViaWebSocket() {
        eventService.sendNotificationViaWebSocket(event, user);

        verify(template).convertAndSend(eq("/topic/notifications"), (Object) any());
    }

    @Test
    void testScheduleNotificationAlternative() {
        User mockedUser = mock(User.class);

        NotificationInterval interval = new NotificationInterval();
        interval.setDayOfWeek(DayOfWeek.MONDAY);
        interval.setStartTime(LocalTime.MIN);
        interval.setEndTime(LocalTime.MAX);

        when(mockedUser.getNotificationIntervals()).thenReturn(Collections.singletonList(interval));

        Event event = new Event();
        event.setEventTime(LocalDateTime.of(2025, Month.JUNE, 2, 10, 0)); // Понедельник, 10:00 утра

        eventService.scheduleNotification(event, mockedUser);

        verify(taskScheduler).schedule(any(Runnable.class), any(Instant.class));
    }

    @Test
    void testPrepareNotificationText() {
        String text = eventService.prepareNotificationText(event, user);

        assertTrue(text.contains("Пользователю John Doe"));
    }

    @Test
    void testIsInNotificationPeriod() {
        NotificationInterval interval = new NotificationInterval();
        interval.setDayOfWeek(DayOfWeek.MONDAY);
        interval.setStartTime(LocalTime.of(9, 0));
        interval.setEndTime(LocalTime.of(17, 0));

        event.setEventTime(LocalDateTime.of(2025, Month.JUNE, 2, 10, 0));

        boolean result = eventService.isInNotificationPeriod(List.of(interval), event.getEventTime());

        assertTrue(result);
    }
}
