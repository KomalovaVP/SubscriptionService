package com.example.notificationservice.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @MessageMapping("/sendNotification")
    @SendTo("/topic/notifications")
    public NotificationPayload sendNotification(String payload) {
        // Формируем объект уведомления
        return new NotificationPayload(payload);
    }
}

    record NotificationPayload(String message) {
}
