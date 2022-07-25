package ru.yandex.practicum.shareIt.item;

import lombok.Data;

import java.util.Optional;

@Data
public class ItemUpdate {
    private Long id;
    private Optional<String> name;
    private Optional<String> description;
    private Optional<Boolean> available;
    private Long userId;
}
