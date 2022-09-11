package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemShort {
    private Long id;
    private String name;
    private Long userId;
    private String description;
    private Boolean available;
    private Long requestId;
}
