package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemController {
    private final ItemService service;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @GetMapping
    public Collection<ItemDto> getAllItemsToUser(@NotNull @RequestHeader("X-Sharer-User-Id") long userId,
                                                 @RequestParam(required = false) @PositiveOrZero Integer from,
                                                 @RequestParam(required = false) @Positive Integer size) {
        Collection<ItemDto> response = service.getItems(userId, from, size);
        log.debug("Returned the list of items: {}", response.size());
        return response;
    }

    @GetMapping("{id}")
    public ItemDto getById(@NotNull @RequestHeader("X-Sharer-User-Id") long userId,
                           @NotNull @PathVariable Long id) {
        ItemDto response = service.getById(id, userId);
        log.debug("Returned the item: {}", response.getId());
        return response;
    }

    @GetMapping("search")
    public Collection<ItemDto> search(@RequestParam String text,
                                      @RequestParam(required = false) @PositiveOrZero Integer from,
                                      @RequestParam(required = false) @Positive Integer size) {
        Collection<ItemDto> response = service.search(text, from, size);
        log.debug("Found items: {}", response.size());
        return response;
    }

    @PostMapping
    public ItemDto add(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                       @Valid @NotNull @RequestBody ItemDto item) {
        ItemDto response = service.add(itemMapper.mapToItem(item), userId);
        log.debug("User added: {}", response.getId());
        return response;
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @NotNull @RequestBody CommentDto comment,
                                 @NotNull @PathVariable Long itemId) {
        CommentDto response = service.addComment(commentMapper.mapToComment(comment, userId, itemId));
        log.debug("Comment added: {}", response.getId());
        return response;
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                          @Valid @NotNull @RequestBody Map<String, Object> updates,
                          @NotNull @PathVariable Long itemId) {
        ItemDto response = service.update(itemMapper.mapToItemUpdate(updates), userId, itemId);
        log.debug("Updated item information: {}", response.getId());
        return response;

    }

    @DeleteMapping("{itemId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") long userId,
                       @PathVariable long itemId) {
        service.delete(userId, itemId);
        log.debug("The item " + itemId + " delete.");
    }
}
