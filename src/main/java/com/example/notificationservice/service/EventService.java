package com.example.notificationservice.service;

import com.example.notificationservice.entity.Event;
import com.example.notificationservice.entity.NotificationInterval;
import com.example.notificationservice.entity.User;
import com.example.notificationservice.dao.EventDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventDao eventDao;
    private final UserService userService;
    private final TaskScheduler taskScheduler;
    private final SimpMessagingTemplate template;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);

    @Autowired
    public EventService(EventDao eventDao, UserService userService, TaskScheduler taskScheduler, SimpMessagingTemplate template) {
        this.eventDao = eventDao;
        this.userService = userService;
        this.taskScheduler = taskScheduler;
        this.template = template;
    }

    /**
     * Возвращает список всех событий.
     */
    public Iterable<Event> findAll() {
        return eventDao.findAll();
    }

    /**
     * Находит событие по идентификатору.
     */
    public Optional<Event> findById(Long id) {
        return eventDao.findById(id);
    }

    /**
     * Обновляет событие.
     */
    public void update(Event event) {
        eventDao.save(event);
    }

    /**
     * Удаляет событие по идентификатору.
     */
    public void delete(Long id) {
        eventDao.deleteById(id);
    }


    /**
     * Создает событие.
     */
    public void createEvent(Event event) {
        eventDao.save(event);

        for (User user : userService.findAll()) {
            if (user.getNotificationIntervals() != null && !user.getNotificationIntervals().isEmpty()) {
                if (isInNotificationPeriod(user.getNotificationIntervals(), event.getEventTime())) {
                    sendNotificationViaWebSocket(event, user);
                } else {
                    scheduleNotification(event, user);
                }
            } else {
                LOGGER.info("Пользователь с ID {} не имеет интервалов информирования.", user.getId());
            }
        }
    }

    void sendNotificationViaWebSocket(Event event, User user) {
        String notificationText = prepareNotificationText(event, user);
        LOGGER.info("Пользователю {} отправлено оповещение через WebSocket с текстом: \"{}\"", user.getFullName(), notificationText);
        template.convertAndSend("/topic/notifications", notificationText);
    }

    void scheduleNotification(Event event, User user) {
        ZonedDateTime nextAvailableTime = findNextAvailableTime(user.getNotificationIntervals());
        LOGGER.info("Запланирована отправка уведомления пользователю {} на {}", user.getFullName(), nextAvailableTime);
        taskScheduler.schedule(() -> sendNotificationViaWebSocket(event, user), nextAvailableTime.toInstant());
    }

    private ZonedDateTime findNextAvailableTime(List<NotificationInterval> intervals) {
        if (intervals == null || intervals.isEmpty()) {
            throw new IllegalArgumentException("Список интервалов не может быть пустым!");
        }

        ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime now = ZonedDateTime.now(zone);

        int iterationLimit = 1000; // Максимальное число итераций

        for (int i = 0; i < iterationLimit; i++) {
            for (NotificationInterval interval : intervals) {
                if (now.getDayOfWeek().equals(interval.getDayOfWeek())) {
                    LocalTime startTime = interval.getStartTime();
                    LocalTime endTime = interval.getEndTime();

                    if (now.toLocalTime().isBefore(endTime)) {
                        return now.with(startTime);
                    }
                }
            }
            now = now.plusDays(1); // Переходим на следующий день
        }
        throw new RuntimeException("Не удалось найти подходящее время отправки уведомления.");
    }

    String prepareNotificationText(Event event, User user) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        String currentTime = LocalDateTime.now().format(formatter);

        return String.format(
                "%s Пользователю %s отправлено оповещение с текстом: \"%s\"",
                currentTime,
                user.getFullName(),
                event.getMessage()
        );
    }

    boolean isInNotificationPeriod(List<NotificationInterval> notificationIntervals, LocalDateTime eventTime) {
        DayOfWeek eventDay = eventTime.getDayOfWeek();
        LocalTime eventTimeOfDay = eventTime.toLocalTime();

        for (NotificationInterval interval : notificationIntervals) {
            if (interval.getDayOfWeek().equals(eventDay)) {
                LocalTime startTime = interval.getStartTime();
                LocalTime endTime = interval.getEndTime();

                if ((eventTimeOfDay.isAfter(startTime) || eventTimeOfDay.equals(startTime)) &&
                        (eventTimeOfDay.isBefore(endTime) || eventTimeOfDay.equals(endTime))) {
                    return true;
                }
            }
        }
        return false;
    }
}
