package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService service;
    private final ItemRequestMapper mapper;

    @PostMapping
    public ItemRequestDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @RequestBody ItemRequestDto itemRequest) {
        ItemRequest request = mapper.mapToRequest(itemRequest);
        request.setUserId(userId);
        return service.add(request);
    }

    @GetMapping
    public Collection<ItemRequestDto> getAllRequestsToUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.getAllByUserId(userId);
    }

    @GetMapping("all")
    public Collection<ItemRequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam Integer from,
                                                     @RequestParam Integer size) {
        return service.getAll(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ItemRequestDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long requestId) {
        return service.getById(userId, requestId);
    }


}
