package com.example.notificationservice.service;

import com.example.notificationservice.entity.User;
import com.example.notificationservice.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Сохраняет нового пользователя в хранилище.
     */
    public void create(User user) {
        userDao.save(user);
    }

    /**
     * Возвращает пользователя по идентификатору.
     */
    public Optional<User> findById(Long id) {
        return userDao.findById(id);
    }

    /**
     * Возвращает список всех зарегистрированных пользователей.
     */
    public Iterable<User> findAll() { return userDao.findAll(); }

    /**
     * Обновляет информацию о пользователе.
     */
    public void update(User updatedUser){ userDao.save(updatedUser); }

    /**
     * Удаляет пользователя по идентификатору.
     */
    public void delete(Long id) {
        userDao.deleteById(id);
    }
}
