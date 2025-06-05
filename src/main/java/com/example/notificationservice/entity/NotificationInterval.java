package com.example.notificationservice.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Embeddable
@Data
public class NotificationInterval implements Serializable {
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public NotificationInterval() {
    }
}
