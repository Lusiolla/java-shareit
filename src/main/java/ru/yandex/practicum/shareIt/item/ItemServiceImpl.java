package ru.yandex.practicum.shareIt.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final ItemMapper mapper;

    @Override
    public Collection<ItemResponse> getItems(long userId) {

        return repository.findByUserId(userId).stream().map(mapper::mapToItemResponse).collect(Collectors.toList());
    }

    @Override
    public ItemResponse getById(long id) {

        return mapper.mapToItemResponse(repository.findByItemId(id));
    }

    @Override
    public ItemResponse add(long userId, Item item) {

        return mapper.mapToItemResponse(repository.saveNewItem(userId, item));
    }

    @Override
    public ItemResponse update(long userId, Item item) {

        return mapper.mapToItemResponse(repository.update(userId, mapper.mapToItemUpdate(item)));
    }

    @Override
    public void delete(long userId, long itemId) {

        repository.deleteByUserIdAndItemId(userId, itemId);
    }

    @Override
    public Collection<ItemResponse> search(String text) {
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
