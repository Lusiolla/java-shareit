package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDTO;
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
    private final ItemMapper mapper;

    @GetMapping
    public Collection<ItemDTO> getAllItemsToUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        Collection<ItemDTO> response = itemService.getItems(userId);
        log.debug("Returned the list of items: {}", response.size());
        return response;
    }

    @GetMapping("{id}")
    public ItemDTO getById(@NotNull @PathVariable Long id) {
        ItemDTO response = itemService.getById(id);
        log.debug("Returned the item: {}", response.getId());
        return response;
    }

    @GetMapping("search")
    public Collection<ItemDTO> search(@RequestParam String text) {
        Collection<ItemDTO> response = itemService.search(text);
        log.debug("Found items: {}", response.size());
        return response;
    }

    @PostMapping
    public ItemDTO add(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @Valid @RequestBody ItemDTO item) {
        ItemDTO response = itemService.add(mapper.mapToItem(item), userId);
        log.debug("User added: {}", response.getId());
        return response;
    }

    @PatchMapping("{itemId}")
    public ItemDTO update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody Map<String, Object> updates,
                          @NotNull @PathVariable Long itemId) {
        ItemDTO response = itemService.update(mapper.mapToItemUpdate(updates), userId, itemId);
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
