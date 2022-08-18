package ru.practicum.shareit.item.srorage;

import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {
    Collection<Item> findByUserId(long userId);

    Item findByItemId(long itemId);

    Item saveNewItem(Item item);

    void saveNewUser(long userId);

    void deleteByUserIdAndItemId(long userId, long itemId);

    void deleteByUserId(long userId);

    Item update(ItemUpdate updateItem);

    Collection<Item> search(String text);
}