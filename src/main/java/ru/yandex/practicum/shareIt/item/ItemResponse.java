package ru.yandex.practicum.shareIt.item;

import lombok.Data;

@Data
public class ItemDTO {
    private Long id;
    private String name;
    private String description;
    private boolean available;
}
