package com.example.addressservice.address;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class AddressRepository {

    private static final Map<Long, Address> users = Map.of(
            20000L, new Address(10000L, "ul. Grunwaldzka", "Słupsk"),
            20001L, new Address(10001L, "ul. Mickiewicza", "Ustka")
    );

    public Optional<Address> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }
}
