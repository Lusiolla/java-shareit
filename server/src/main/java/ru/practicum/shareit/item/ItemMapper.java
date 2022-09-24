package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;


import java.util.ArrayList;
import java.util.Collection;
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
        response.setRequestId(item.getRequestId());
        return response;
    }

    // из item в short
    public ItemShort mapToItemShort(Item item) {
        ItemShort response = new ItemShort();
        response.setId(item.getId());
        response.setName(item.getName());
        response.setUserId(item.getUserId());
        response.setDescription(item.getDescription());
        response.setAvailable(item.getAvailable());
        response.setRequestId(item.getRequestId());
        return response;
    }

    // из dto в item
    public Item mapToItem(ItemDto dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setRequestId(dto.getRequestId());
        return item;
    }

    // из item в item-with-booking
    public ItemWithBooking mapToItemWithBooking(Item item, BookingShort last, BookingShort next) {
        ItemWithBooking response = new ItemWithBooking();
        response.setId(item.getId());
        response.setName(item.getName());
        response.setDescription(item.getDescription());
        response.setAvailable(item.getAvailable());
        response.setRequestId(item.getRequestId());
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

    // из iterable в dto
    public Collection<ItemDto> mapToItemDto(Iterable<Item> items) {
        Collection<ItemDto> response = new ArrayList<>();
        for (Item item : items) {
            response.add(mapToItemDto(item));
        }
        return response;
    }
}

