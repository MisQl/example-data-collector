package com.example.userservice.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {

    private Long id;
    private String firstname;
    private String lastname;
    private Long parentId;
}
