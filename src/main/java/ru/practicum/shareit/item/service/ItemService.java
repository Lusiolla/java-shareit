package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;


import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> getItems(long userId);

    ItemDto getById(long id, long userId);

    ItemDto add(Item item, long userId);

    CommentDto addComment(Comment comment);

    ItemDto update(ItemUpdate item, long userId, long itemId);

    void delete(long userId, long itemId);

    Collection<ItemDto> search(String text);

}
