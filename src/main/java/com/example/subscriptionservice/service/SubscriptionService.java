package com.example.subscriptionservice.service;

import com.example.subscriptionservice.model.Subscription;
import com.example.subscriptionservice.model.User;
import com.example.subscriptionservice.repository.SubscriptionRepository;
import com.example.subscriptionservice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionService.class);

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }

    // Добавление подписки пользователю
    public Subscription addSubscription(Long userId, Subscription subscription) {
        LOGGER.info("Adding subscription to user with id {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));

        subscription.setUser(user);

        return subscriptionRepository.save(subscription);
    }

    // Получение списка подписок пользователя
    public List<Subscription> getSubscriptionsForUser(Long userId) {
        LOGGER.debug("Fetching subscriptions for user with id {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        return subscriptionRepository.findByUser(user);
    }

    // Удаление подписки
    public void deleteSubscription(Long subscriptionId) {
        LOGGER.info("Deleting subscription with id {}", subscriptionId);
        subscriptionRepository.deleteById(subscriptionId);
    }

    // Получить топ-3 популярных подписок
    public List<String> getTopThreePopularSubscriptions() {
        LOGGER.debug("Fetching top three most popular subscriptions");
        return subscriptionRepository.findTop3MostPopularSubscriptions();
    }
}
