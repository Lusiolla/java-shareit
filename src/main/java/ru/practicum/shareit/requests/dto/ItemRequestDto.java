package ru.practicum.shareit.requests.dto;

import lombok.Data;

@Data
public class ItemRequestDto {
    private Long id;
    private String name;
    private String description;
}
