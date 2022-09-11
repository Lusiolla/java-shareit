package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService service;
    private final ItemRequestMapper mapper;

    @PostMapping
    public ItemRequestDto add(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                              @Valid @NotNull @RequestBody ItemRequestDto itemRequest) {
        ItemRequest request = mapper.mapToRequest(itemRequest);
        request.setUserId(userId);
        ItemRequestDto response = service.add(request);
        log.debug("Request added: {}", response.getId());
        return response;
    }

    @GetMapping
    public Collection<ItemRequestDto> getAllRequestsToUser(@NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        Collection<ItemRequestDto> response = service.getAllByUserId(userId);
        log.debug("Returned the list of requests: {}", response.size());
        return response;
    }

    @GetMapping("all")
    public Collection<ItemRequestDto> getAllRequests(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @RequestParam(required = false) @PositiveOrZero Integer from,
                                                     @RequestParam(required = false) @Positive Integer size) {
        Collection<ItemRequestDto> response = service.getAll(userId, from, size);
        log.debug("Returned the list of requests: {}", response.size());
        return response;
    }

    @GetMapping("{requestId}")
    public ItemRequestDto getById(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                  @NotNull @PathVariable long requestId) {
        ItemRequestDto response = service.getById(userId, requestId);
        log.debug("Returned the requests: {}", response.getId());
        return response;
    }


}
