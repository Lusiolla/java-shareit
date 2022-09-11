package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto add(ItemRequest itemRequest);

    Collection<ItemRequestDto> getAllByUserId(long userId);

    Collection<ItemRequestDto> getAll(long userId, Integer from, Integer size);

    ItemRequestDto getById(long userId, long id);
}
