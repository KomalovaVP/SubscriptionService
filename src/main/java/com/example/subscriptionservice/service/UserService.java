package com.example.subscriptionservice.service;

import com.example.subscriptionservice.model.User;
import com.example.subscriptionservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {
    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Создание пользователя
    public User createUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username must be provided");
        }
        LOGGER.info("Creating user with username {}", user.getUsername());
        return userRepository.save(user);
    }

    // Получение информации о пользователе
    public User getUserById(Long id) {
        LOGGER.debug("Getting user with id {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
    }

    // Обновление пользователя по идентификатору
    public User updateUser(Long id, User userDetails) {
        LOGGER.debug("Updating user with id {}", id);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        existingUser.setUsername(userDetails.getUsername());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setUpdated(new Date());

        return userRepository.save(existingUser);
    }

    // Удаление пользователя
    public void deleteUser(Long id) {
        LOGGER.info("Deleting user with id {}", id);
        userRepository.deleteById(id);
    }
}
