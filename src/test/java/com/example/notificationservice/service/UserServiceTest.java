package com.example.notificationservice.service;

import com.example.notificationservice.dao.UserDao;
import com.example.notificationservice.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserDao userDao;

    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setId(1L);
        user.setFullName("John Doe");
    }

    @Test
    void testCreateUser() {
        when(userDao.save(any(User.class))).thenReturn(user);

        userService.create(user);

        verify(userDao).save(user);
    }

    @Test
    void testFindById() {
        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    void testFindAll() {
        when(userDao.findAll()).thenReturn(Collections.singletonList(user));

        Iterable<User> result = userService.findAll();

        assertEquals(1, ((Iterable<?>)result).spliterator().estimateSize());
    }

    @Test
    void testUpdateUser() {
        when(userDao.save(any(User.class))).thenReturn(user);

        userService.update(user);

        verify(userDao).save(user);
    }

    @Test
    void testDeleteUser() {
        userService.delete(1L);

        verify(userDao).deleteById(1L);
    }
}
