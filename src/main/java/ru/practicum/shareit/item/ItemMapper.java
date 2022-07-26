package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.item.model.Item;


import java.util.Map;
import java.util.Optional;

@Component
public class ItemMapper {

    //из item в dto
    public ItemDTO mapToItemResponse(Item item) {
        ItemDTO response = new ItemDTO();
        response.setId(item.getId());
        response.setName(item.getName());
        response.setDescription(item.getDescription());
        response.setAvailable(item.getAvailable());
        return response;
    }

    //из dto в item
    public Item mapToItem(ItemDTO dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        return item;
    }

    //из map в update
    public ItemUpdate mapToItemUpdate(Map<String, Object> updates) {
        ItemUpdate update = new ItemUpdate();
        update.setName(updates.get("name") != null ? Optional.of(updates.get("name").toString()) : Optional.empty());
        update.setDescription(updates.get("description") != null ? Optional.of(updates.get("description").toString())
                : Optional.empty());
        update.setAvailable(updates.get("available") != null ? Optional.of((Boolean) updates.get("available"))
                : Optional.empty());
        return update;
    }


}

