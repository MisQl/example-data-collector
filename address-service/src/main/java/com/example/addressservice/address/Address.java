package com.example.addressservice.address;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Address {

    private Long id;
    private String street;
    private String city;
}
