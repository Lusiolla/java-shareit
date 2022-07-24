package ru.yandex.practicum.shareIt.item;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();

    private long id;

    @Override
    public List<Item> findByUserId(long userId) {
       return items.values().stream().filter(item -> item.getUserId() == userId).collect(Collectors.toList());
    }

    @Override
    public Item save(Item item) {
        id = id + 1;
        item.setId(id);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        if (itemId == 0) {
            items.values().stream().map(Item::getId).filter(aLong -> userId == aLong).forEach(items::remove);
        } else {
            items.remove(itemId);
        }

    }
}
