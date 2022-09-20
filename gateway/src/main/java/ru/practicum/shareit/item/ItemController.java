package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.ItemRequest;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Map;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getItems(@NotNull @RequestHeader("X-Sharer-User-Id") long userId,
                                           @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug("Returned the list of items");
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getItem(@NotNull @RequestHeader("X-Sharer-User-Id") long userId,
                                          @NotNull @PathVariable Long id) {
        log.debug("Returned the item: {}", id);
        return itemClient.getItem(userId, id);
    }

    @GetMapping("search")
    public ResponseEntity<Object> searchForItems(
            @NotNull @RequestParam String text,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug("Search items");
        return itemClient.searchForItems(text, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addNewItem(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Valid @NotNull @RequestBody ItemRequest item) {
        log.debug("Item added: {}", item);
        return itemClient.addNewItem(userId, item);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> addNewComment(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                                @Valid @NotNull @RequestBody CommentRequest comment,
                                                @NotNull @PathVariable Long itemId) {
        log.debug("Comment added: {}", comment);
        return itemClient.addNewComment(userId, comment, itemId);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> updateItem(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Valid @NotNull @RequestBody Map<String, Object> updates,
                                             @NotNull @PathVariable Long itemId) {
        log.debug("Updated item information: {}", itemId);
        return itemClient.updateItem(userId, updates, itemId);

    }

    @DeleteMapping("{itemId}")
    public ResponseEntity<Object> deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId) {
        log.debug("The item " + itemId + " delete.");
        return itemClient.deleteItem(userId, itemId);

    }
}
