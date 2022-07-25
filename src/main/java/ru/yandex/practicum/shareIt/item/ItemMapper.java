package ru.yandex.practicum.shareIt.item;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ItemMapper {

    //из item в response
    public ItemResponse mapToItemResponse(Item item) {
        ItemResponse response = new ItemResponse();
        response.setId(item.getId());
        response.setName(item.getName());
        response.setDescription(item.getDescription());
        response.setAvailable(item.getAvailable());
        return response;
    }

    //из response в item
    public Item mapToItem(ItemResponse response) {
        Item item = new Item();
        item.setId(response.getId());
        item.setName(response.getName());
        item.setDescription(response.getDescription());
        item.setAvailable(response.isAvailable());
        return item;
    }

    //из item в update
    public ItemUpdate mapToItemUpdate(Item item) {
        ItemUpdate update = new ItemUpdate();
        update.setId(item.getId());
        update.setName(Optional.ofNullable(item.getName()));
        update.setDescription(Optional.ofNullable(item.getDescription()));
        update.setAvailable(Optional.ofNullable(item.getAvailable()));
        update.setUserId(item.getUserId());
        return update;
    }


}

