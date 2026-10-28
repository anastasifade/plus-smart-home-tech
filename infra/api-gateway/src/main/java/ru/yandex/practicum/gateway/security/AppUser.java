package ru.yandex.practicum.gateway.security;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AppUser {
    private String username;
    private String password;
    private List<Role> roles;
}
