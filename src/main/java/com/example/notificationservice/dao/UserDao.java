package com.example.notificationservice.dao;

import com.example.notificationservice.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Long> {
}
