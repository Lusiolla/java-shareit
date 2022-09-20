package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @GetMapping
    public Collection<ItemDto> getAllItemsToUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam Integer from,
                                                 @RequestParam Integer size) {
        return service.getItems(userId, from, size);
    }

    @GetMapping("{id}")
    public ItemDto getById(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable Long id) {
        return service.getById(id, userId);
    }

    @GetMapping("search")
    public Collection<ItemDto> search(@RequestParam String text,
                                      @RequestParam Integer from,
                                      @RequestParam Integer size) {
        return service.search(text, from, size);
    }

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @RequestBody ItemDto item) {
        return service.add(itemMapper.mapToItem(item), userId);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @RequestBody CommentDto comment,
                                 @PathVariable Long itemId) {
        return service.addComment(commentMapper.mapToComment(comment, userId, itemId));
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody ItemUpdate updateItem,
                          @PathVariable Long itemId) {
        return service.update(updateItem, userId, itemId);

    }

    @DeleteMapping("{itemId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") long userId,
                       @PathVariable long itemId) {
        service.delete(userId, itemId);
    }
}
