package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreate;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingService {
    Collection<BookingDto> getAllBookingByUserId(long userId, State state, Integer from, Integer size);

    Collection<BookingDto> getAllBookedItemsByUserId(long userId, State state, Integer from, Integer size);

    BookingCreate add(Booking newBooking, long userId);

    BookingDto approved(long userId, long bookingId, boolean isApproved);

    BookingDto getById(long id, long userId);
}
