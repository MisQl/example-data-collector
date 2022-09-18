package com.example.userservice.user;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class UserRepository {

    private static final Map<Long, User> users = Map.of(
            10000L, new User(10000L, "Jan", "Nowak", 10010L),
            10001L, new User(10001L, "Ola", "Kowalska", 10011L),
            10010L, new User(10010L, "Andrzej", "Nowak", null),
            10011L, new User(10011L, "Marek", "Wisniewski", null)
    );

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }
}
