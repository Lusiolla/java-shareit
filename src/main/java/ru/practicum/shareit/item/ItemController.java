package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;

    @GetMapping
    public Collection<ItemDto> getAllItemsToUser(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        Collection<ItemDto> response = itemService.getItems(userId);
        log.debug("Returned the list of items: {}", response.size());
        return response;
    }

    @GetMapping("{id}")
    public ItemDto getById(@Valid @NotNull@RequestHeader("X-Sharer-User-Id") long userId,
                           @Valid @NotNull @PathVariable Long id) {
        ItemDto response = itemService.getById(id, userId);
        log.debug("Returned the item: {}", response.getId());
        return response;
    }

    @GetMapping("search")
    public Collection<ItemDto> search(@RequestParam String text) {
        Collection<ItemDto> response = itemService.search(text);
        log.debug("Found items: {}", response.size());
        return response;
    }

    @PostMapping
    public ItemDto add(@Valid @NotNull@RequestHeader("X-Sharer-User-Id") Long userId,
                       @Valid @NotNull @RequestBody ItemDto item) {
        ItemDto response = itemService.add(itemMapper.mapToItem(item), userId);
        log.debug("User added: {}", response.getId());
        return response;
    }

    @PostMapping("{itemId}/comment")
    public CommentDto addComment(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @NotNull @RequestBody CommentDto comment,
                                 @Valid @NotNull @PathVariable Long itemId) {
        CommentDto response = itemService.addComment(commentMapper.mapToComment(comment, userId, itemId));
        log.debug("Comment added: {}", response.getId());
        return response;
    }

    @PatchMapping("{itemId}")
    public ItemDto update(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                          @Valid @NotNull @RequestBody Map<String, Object> updates,
                          @Valid @NotNull @PathVariable Long itemId) {
        ItemDto response = itemService.update(itemMapper.mapToItemUpdate(updates), userId, itemId);
        log.debug("Updated item information: {}", response.getId());
        return response;

    }

    @DeleteMapping("{itemId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") long userId,
                       @PathVariable long itemId) {
        itemService.delete(userId, itemId);
        log.debug("The item " + itemId + " delete.");
    }
}
