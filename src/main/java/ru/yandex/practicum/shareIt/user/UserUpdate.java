package ru.yandex.practicum.shareIt.user;

import lombok.Data;

import java.util.Optional;

@Data
public class UserUpdate {
    private Long id;
    private Optional<String> email;
    private Optional<String> name;
}
