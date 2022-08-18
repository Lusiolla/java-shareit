package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.item.model.Item;


import java.util.Collection;

public interface ItemService {
    Collection<ItemDTO> getItems(long userId);

    ItemDTO getById(long id);

    ItemDTO add(Item item, long userId);

    ItemDTO update(ItemUpdate item, long userId, long itemId);

    void delete(long userId, long itemId);

    Collection<ItemDTO> search(String text);

}
