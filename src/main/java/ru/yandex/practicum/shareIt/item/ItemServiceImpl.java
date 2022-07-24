package ru.yandex.practicum.shareIt.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    @Override
    public List<Item> getItems(long userId) {
        return null;
    }

    @Override
    public Item addNewItem(long userId, Item item) {
        return null;
    }

    @Override
    public void deleteItem(long userId, long itemId) {

    }
}
