package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreate;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class BookingMapper {

    // из booking в dto
    public BookingDto mapToBookingDto(Booking booking) {
        BookingDto response = new BookingDto();
        response.setBooker(booking.getUser());
        response.setItem(booking.getItem());
        response.setId(booking.getId());
        response.setStart(booking.getStartBooking());
        response.setEnd(booking.getEndBooking());
        response.setStatus(booking.getStatus());
        return response;
    }

    // из booking в create
    public BookingCreate mapToBookingCrate(Booking booking) {
        BookingCreate response = new BookingCreate();
        response.setBookerId(booking.getUser().getId());
        response.setItemId(booking.getItem().getId());
        response.setId(booking.getId());
        response.setStart(booking.getStartBooking());
        response.setEnd(booking.getEndBooking());
        return response;
    }

    // из booking в short
    public BookingShort mapToBookingShort(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingShort response = new BookingShort();
        response.setId(booking.getId());
        response.setBookerId(booking.getUser().getId());
        response.setStart(booking.getStartBooking());
        response.setEnd(booking.getEndBooking());
        return response;
    }

    // из create в booking
    public Booking mapToBooking(BookingCreate dto) {
        Booking booking = new Booking();
        Item item = new Item();
        item.setId(dto.getItemId());
        booking.setId(dto.getId());
        booking.setStartBooking(dto.getStart());
        booking.setEndBooking(dto.getEnd());
        booking.setStatus(dto.getStatus());
        booking.setItem(item);
        return booking;
    }

    // из iterable в dto
    public Collection<BookingDto> mapToBookingDto(Iterable<Booking> bookings) {
        Collection<BookingDto> response = new ArrayList<>();
        for (Booking booking : bookings) {
            response.add(mapToBookingDto(booking));
        }
        return response;
    }
}
