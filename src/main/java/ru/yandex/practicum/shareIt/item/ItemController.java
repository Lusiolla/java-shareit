package ru.yandex.practicum.shareIt.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public Collection<ItemResponse> getAllItemsToUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        Collection<ItemResponse> response = itemService.getItems(userId);
        log.debug("Returned the list of items: {}", response.size());
        return response;
    }

    @GetMapping("{id}")
    public ItemResponse getById(@NotNull @PathVariable Long id) {
        ItemResponse item = itemService.getById(id);
        log.debug("Returned the item: {}", item.getId());
        return item;
    }

    @GetMapping("search")
    public Collection<ItemResponse> search(@RequestParam String text) {
        Collection<ItemResponse> response = itemService.search(text);
        log.debug("Found items: {}", response.size());
        return response;
    }

    @PostMapping
    public ItemResponse add(@RequestHeader("X-Sharer-User-Id") Long userId,
                            @Valid @RequestBody Item item) {
        item.setUserId(userId);
        ItemResponse response = itemService.add(userId, item);
        log.debug("User added: {}", response.getId());
        return response;
    }

    @PatchMapping("{itemId}")
    public ItemResponse update(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @RequestBody Item updateItem,
                               @NotNull @PathVariable Long itemId) {
        updateItem.setId(itemId);
        updateItem.setUserId(userId);
        ItemResponse item = itemService.update(userId, updateItem);
        log.debug("Updated item information: {}", item.getId());
        return item;
    }

    @DeleteMapping("{itemId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") long userId,
                       @PathVariable long itemId) {
        itemService.delete(userId, itemId);
        log.debug("The item " + itemId + " delete.");
    }
}