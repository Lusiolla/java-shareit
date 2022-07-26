package ru.practicum.shareit.user.dto;

import lombok.Data;

import java.util.Optional;

@Data
public class UserUpdate {
    private Long id;
    private Optional<String> email;
    private Optional<String> name;
}
