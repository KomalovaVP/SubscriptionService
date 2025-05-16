package com.example.subscriptionservice.controller;

import com.example.subscriptionservice.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    // GET /subscriptions/top - получить ТОП-3 популярных подписок
    @GetMapping("/top")
    public ResponseEntity<List<String>> getTop3MostPopularSubscriptions() {
        List<String> popularSubscriptions = subscriptionService.getTopThreePopularSubscriptions();
        return ResponseEntity.ok(popularSubscriptions);
    }
}
