package ru.yandex.practicum.shareIt.item;

import java.util.Collection;

public interface ItemService {
    Collection<ItemResponse> getItems(long userId);

    ItemResponse getById(long id);

    ItemResponse add(long userId, Item item);

    ItemResponse update(long userId, Item item);

    void delete(long userId, long itemId);

    Collection<ItemResponse> search(String text);

}
