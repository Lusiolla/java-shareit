package ru.yandex.practicum.shareIt.item;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.shareIt.exeption.ItemNotFoundException;
import ru.yandex.practicum.shareIt.exeption.UserNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Set<Item>> usersItems = new HashMap<>();
    private final Map<Long, Item> items = new HashMap<>();

    private long id;


    @Override
    public Collection<Item> findByUserId(long userId) {
        if (!usersItems.containsKey(userId)) {
            throw new UserNotFoundException();
        }
        return usersItems.get(userId);
    }

    @Override
    public Item findByItemId(long itemId) {
        return items.get(itemId);
    }

    @Override
    public Item saveNewItem(long userId, Item item) {
        if (!usersItems.containsKey(userId)) {
            throw new UserNotFoundException();
        }
        id = id + 1;
        item.setId(id);
        usersItems.get(userId).add(item);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public void saveNewUser(long userId) {
        usersItems.put(userId, new HashSet<>());
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        if (!usersItems.containsKey(userId)) {
            throw new UserNotFoundException();
        }
        usersItems.get(userId).removeIf(item -> item.getId() == itemId);
        items.remove(itemId);
    }

    @Override
    public void deleteByUserId(long userId) {
        usersItems.remove(userId);
        items.values().stream().map(Item::getUserId).filter(aLong -> userId == aLong).forEach(items::remove);
    }

    @Override
    public Item update(long userId, ItemUpdate updateItem) {
        Item itemFromRepository = items.get(updateItem.getId());
        if (itemFromRepository == null || itemFromRepository.getUserId() != userId) {
            throw new ItemNotFoundException();
        }
        if (updateItem.getName().isPresent()) {
            itemFromRepository.setName(updateItem.getName().get());
        }

        if (updateItem.getDescription().isPresent()) {
            itemFromRepository.setDescription(updateItem.getDescription().get());
        }

        if (updateItem.getAvailable().isPresent()) {
            itemFromRepository.setAvailable(updateItem.getAvailable().get());
        }
        return itemFromRepository;
    }

    @Override
    public Collection<Item> search(String text) {
        return items.values()
                .stream()
                .filter(item -> item.getAvailable() && (item.getDescription().toLowerCase().contains(text)
                        || item.getName().toLowerCase().contains(text)))
                .collect(Collectors.toList());
    }
}
