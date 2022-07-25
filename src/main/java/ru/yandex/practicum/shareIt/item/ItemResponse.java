package ru.yandex.practicum.shareIt.item;

import lombok.Data;

@Data
public class ItemResponse {
    private Long id;
    private String name;
    private String description;
    private boolean available;
}
