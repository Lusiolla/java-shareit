package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.srorage.ItemRepository;
import ru.practicum.shareit.item.ItemMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final ItemMapper mapper;

    @Override
    public Collection<ItemDTO> getItems(long userId) {

        return repository.findByUserId(userId).stream().map(mapper::mapToItemResponse).collect(Collectors.toList());
    }

    @Override
    public ItemDTO getById(long id) {

        return mapper.mapToItemResponse(repository.findByItemId(id));
    }

    @Override
    public ItemDTO add(Item item, long userId) {
        item.setUserId(userId);
        return mapper.mapToItemResponse(repository.saveNewItem(item));
    }

    @Override
    public ItemDTO update(ItemUpdate item, long userId, long itemId) {
        item.setUserId(userId);
        item.setId(itemId);
        return mapper.mapToItemResponse(repository.update(item));
    }

    @Override
    public void delete(long userId, long itemId) {

        repository.deleteByUserIdAndItemId(userId, itemId);
    }

    @Override
    public Collection<ItemDTO> search(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        } else {
            return repository.search(text.toLowerCase())
                    .stream()
                    .map(mapper::mapToItemResponse)
                    .collect(Collectors.toList());
        }
    }
}
