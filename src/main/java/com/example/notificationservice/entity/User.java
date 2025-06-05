package com.example.notificationservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<NotificationInterval> notificationIntervals;
}
