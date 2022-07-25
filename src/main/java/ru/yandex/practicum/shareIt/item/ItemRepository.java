package ru.yandex.practicum.shareIt.item;

import java.util.Collection;

public interface ItemRepository {
    Collection<Item> findByUserId(long userId);

    Item findByItemId(long itemId);

    Item saveNewItem(long userId, Item item);

    void saveNewUser(long userId);

    void deleteByUserIdAndItemId(long userId, long itemId);

    void deleteByUserId(long userId);

    Item update(long userId, ItemUpdate updateItem);

    Collection<Item> search(String text);
}