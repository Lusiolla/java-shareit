package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

  private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> add(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                      @Valid @NotNull @RequestBody ItemRequestDto itemRequest) {
        log.debug("Request added: {}", itemRequest);
        return itemRequestClient.addNewRequest(userId, itemRequest);
    }

    @GetMapping
    public ResponseEntity<Object> getAllRequestsToUser(@NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        log.debug("Returned the list of requests");
        return itemRequestClient.getRequests(userId);
    }

    @GetMapping("all")
    public ResponseEntity<Object> getAllRequests(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.debug("Returned the list of requests");
        return itemRequestClient.getRequestToUser(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getById(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                  @NotNull @PathVariable long requestId) {
        log.debug("Returned the requests: {}", requestId);
        return itemRequestClient.getRequest(userId, requestId);
    }

}
