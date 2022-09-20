package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemUpdate {
    private Long id;
    private Optional<String> name;
    private Optional<String> description;
    private Optional<Boolean> available;
    private Long userId;
}
