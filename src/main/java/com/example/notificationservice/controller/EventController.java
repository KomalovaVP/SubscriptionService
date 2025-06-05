package com.example.notificationservice.controller;

import com.example.notificationservice.entity.Event;
import com.example.notificationservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<?> addEvent(@RequestBody Event event) {
        eventService.createEvent(event);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Получает список всех событий.
     */
    @GetMapping
    public ResponseEntity<Iterable<Event>> getAllEvents() {
        Iterable<Event> events = eventService.findAll();
        return ResponseEntity.ok(events);
    }

    /**
     * Получает конкретное событие по идентификатору.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Optional<Event> event = eventService.findById(id);
        return event.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Обновляет существующее событие.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEvent(@PathVariable Long id, @RequestBody Event event) {
        eventService.update(event);
        return ResponseEntity.noContent().build();
    }

    /**
     * Удаляет событие по идентификатору.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
