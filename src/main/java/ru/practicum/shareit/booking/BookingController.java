package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreate;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper mapper;

    @PostMapping
    public BookingCreate add(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                             @Valid @RequestBody BookingCreate newBooking) {
        BookingCreate booking = bookingService.add(mapper.mapToBooking(newBooking), userId);
        log.debug("Booking added: {}", booking.getId());
        return booking;
    }

    @PatchMapping("{bookingId}")
    public BookingDto approved(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                               @Valid @NotNull @PathVariable Long bookingId,
                               @Valid @NotNull @RequestParam Boolean approved) {
        BookingDto booking = bookingService.approved(userId, bookingId, approved);
        log.debug("Booking update: {}", booking.getStatus());
        return booking;
    }

    @GetMapping("{id}")
    public BookingDto getById(@Valid @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                              @Valid @NotNull @PathVariable Long id) {
        BookingDto response = bookingService.getById(id, userId);
        log.debug("Returned the item: {}", response.getId());
        return response;
    }

    @GetMapping
    public Collection<BookingDto> getAllBookingByUserId(@Valid @NotNull @RequestHeader("X-Sharer-User-Id")
                                                        Long userId,
                                                        @RequestParam(defaultValue = "ALL", required = false)
                                                        State state) {
        Collection<BookingDto> response = bookingService.getAllBookingByUserId(userId, state);
        log.debug("Returned booking(s): {}", response.size());
        return response;
    }

    @GetMapping("owner")
    public Collection<BookingDto> getAllBookingItemsByUserId(@Valid @NotNull @RequestHeader("X-Sharer-User-Id")
                                                             Long userId,
                                                             @RequestParam(defaultValue = "ALL", required = false)
                                                             State state) {
        Collection<BookingDto> response = bookingService.getAllBookingItemsByUserId(userId, state);
        log.debug("Returned booking item(s): {}", response.size());
        return response;
    }

}
