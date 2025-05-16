package com.example.subscriptionservice.repository;

import com.example.subscriptionservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
