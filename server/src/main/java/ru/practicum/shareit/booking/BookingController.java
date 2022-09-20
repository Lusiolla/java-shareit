package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreate;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper mapper;

    @PostMapping
    public BookingCreate add(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestBody BookingCreate newBooking) {
        return bookingService.add(mapper.mapToBooking(newBooking), userId);
    }

    @PatchMapping("{bookingId}")
    public BookingDto approved(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable Long bookingId,
                               @RequestParam Boolean approved) {
        return bookingService.approved(userId, bookingId, approved);
    }

    @GetMapping("{id}")
    public BookingDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long id) {
        return bookingService.getById(id, userId);
    }

    @GetMapping
    public Collection<BookingDto> getAllBookingByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam State state,
                                                        @RequestParam Integer from,
                                                        @RequestParam Integer size) {
        return  bookingService.getAllBookingByUserId(userId, state, from, size);
    }

    @GetMapping("owner")
    public Collection<BookingDto> getAllBookingItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                             @RequestParam State state,
                                                             @RequestParam Integer from,
                                                             @RequestParam Integer size) {
        return bookingService.getAllBookedItemsByUserId(userId, state, from, size);
    }
}
