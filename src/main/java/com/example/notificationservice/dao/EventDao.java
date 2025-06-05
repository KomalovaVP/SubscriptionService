package com.example.notificationservice.dao;

import com.example.notificationservice.entity.Event;
import org.springframework.data.repository.CrudRepository;


public interface EventDao extends CrudRepository<Event, Long> {
}
