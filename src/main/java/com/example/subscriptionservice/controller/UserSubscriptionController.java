package com.example.subscriptionservice.controller;

import com.example.subscriptionservice.model.Subscription;
import com.example.subscriptionservice.model.User;
import com.example.subscriptionservice.service.SubscriptionService;
import com.example.subscriptionservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserSubscriptionController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;

    @Autowired
    public UserSubscriptionController(UserService userService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    // POST /users - создание нового пользователя
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return ResponseEntity.created(URI.create("/users/" + savedUser.getId())).body(savedUser);
    }

    // GET /users/{id} - получение информации о пользователе
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // PUT /users/{id} - обновление данных пользователя
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE /users/{id} - удаление пользователя
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // POST /users/{id}/subscriptions - добавить подписку пользователю
    @PostMapping("/{userId}/subscriptions")
    public ResponseEntity<Subscription> addSubscription(@PathVariable Long userId, @RequestBody Subscription subscription) {
        Subscription addedSubscription = subscriptionService.addSubscription(userId, subscription);
        return ResponseEntity.created(URI.create("/users/" + userId + "/subscriptions/" + addedSubscription.getId())).body(addedSubscription);
    }

    // GET /users/{id}/subscriptions - получить подписки пользователя
    @GetMapping("/{userId}/subscriptions")
    public ResponseEntity<List<Subscription>> getSubscriptionsForUser(@PathVariable Long userId) {
        List<Subscription> subscriptions = subscriptionService.getSubscriptionsForUser(userId);
        return ResponseEntity.ok(subscriptions);
    }

    // DELETE /users/{id}/subscriptions/{sub_id} - удалить подписку пользователя
    @DeleteMapping("/{userId}/subscriptions/{subId}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long userId, @PathVariable Long subId) {
        subscriptionService.deleteSubscription(subId);
        return ResponseEntity.noContent().build();
    }
}
