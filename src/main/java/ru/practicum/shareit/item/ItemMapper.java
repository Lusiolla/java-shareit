package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.item.dto.ItemWithBooking;
import ru.practicum.shareit.item.model.Item;


import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemMapper {

    private final CommentMapper commentMapper;

    // из item в dto
    public ItemDto mapToItemDto(Item item) {
        ItemDto response = new ItemDto();
        response.setId(item.getId());
        response.setName(item.getName());
        response.setDescription(item.getDescription());
        response.setAvailable(item.getAvailable());
        response.setComments(mapToCommentDto(item));
        return response;
    }

    // из dto в item
    public Item mapToItem(ItemDto dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        return item;
    }

    // из map в update
    public ItemUpdate mapToItemUpdate(Map<String, Object> updates) {
        ItemUpdate update = new ItemUpdate();
        update.setName(updates.get("name") != null ? Optional.of(updates.get("name").toString()) : Optional.empty());
        update.setDescription(updates.get("description") != null ? Optional.of(updates.get("description").toString())
                : Optional.empty());
        update.setAvailable(updates.get("available") != null ? Optional.of((Boolean) updates.get("available"))
                : Optional.empty());
        return update;
    }

    // из item в item-with-booking
    public ItemWithBooking mapToItemWithBooking(Item item, BookingShort last, BookingShort next) {
        ItemWithBooking response = new ItemWithBooking();
        response.setId(item.getId());
        response.setName(item.getName());
        response.setDescription(item.getDescription());
        response.setAvailable(item.getAvailable());
        response.setComments(mapToCommentDto(item));
        response.setLastBooking(last);
        response.setNextBooking(next);
        return response;
    }

    private Collection<CommentDto> mapToCommentDto(Item item) {
        if (item.getComments() != null) {
            return item.getComments().stream()
                    .map(commentMapper::mapToCommentDto)
                    .collect(Collectors.toList());
        }
        return null;
    }
}

