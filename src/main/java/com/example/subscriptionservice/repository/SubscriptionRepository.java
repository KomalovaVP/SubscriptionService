package com.example.subscriptionservice.repository;

import com.example.subscriptionservice.model.Subscription;
import com.example.subscriptionservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findByUser(User user);

    @Query(
            value = """
                        SELECT name
                        FROM subscriptions
                        GROUP BY name
                        ORDER BY COUNT(DISTINCT user_id) DESC
                        LIMIT 3
                    """,
            nativeQuery = true
    )
    List<String> findTop3MostPopularSubscriptions();
}
