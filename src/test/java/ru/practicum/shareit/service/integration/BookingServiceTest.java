package ru.practicum.shareit.service.integration;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.practicum.shareit.TestUtil.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {

    private final BookingService service;
    private final BookingMapper mapper;
    private final UserService userService;
    private final ItemService itemService;

    @BeforeTransaction
    public void setUp() {
        userService.add(user1);
        userService.add(user2);
        itemService.add(item1, 1L);
        itemService.add(item2, 1L);
        itemService.add(item3, 2L);
        itemService.add(item4, 2L);
        service.add(booking1, 1L);
        service.add(booking2, 2L);
        service.add(booking3, 1L);
        service.add(booking4, 1L);
    }

    @Test
    public void shouldGetAllBooking() {
        List<Booking> entityBooking = List.of(
                booking1,
                booking3,
                booking4
        );

        Collection<BookingDto> sourceBookings = entityBooking.stream().map(mapper::mapToBookingDto)
                .collect(Collectors.toList());

        Collection<BookingDto> targetBookings = service.getAllBookingByUserId(
                1L, State.ALL, null, null);

        assertThat(targetBookings, hasSize(sourceBookings.size()));
        for (BookingDto sourceBooking : sourceBookings) {
            assertThat(targetBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(sourceBooking.getStart())),
                    hasProperty("end", equalTo(sourceBooking.getEnd())),
                    hasProperty("status", equalTo(sourceBooking.getStatus()))
            )));
        }
    }

    @Test
    public void shouldGetCurrentBooking() {
        List<Booking> entityBooking = List.of(
                booking4
        );

        Collection<BookingDto> sourceBookings = entityBooking.stream().map(mapper::mapToBookingDto)
                .collect(Collectors.toList());

        Collection<BookingDto> targetBookings = service.getAllBookingByUserId(
                1L, State.CURRENT, null, null);

        assertThat(targetBookings, hasSize(sourceBookings.size()));
        for (BookingDto sourceBooking : sourceBookings) {
            assertThat(targetBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(sourceBooking.getStart())),
                    hasProperty("end", equalTo(sourceBooking.getEnd())),
                    hasProperty("status", equalTo(sourceBooking.getStatus()))
            )));
        }
    }

    @Test
    public void shouldGetFutureBooking() {
        List<Booking> entityBooking = List.of(
                booking3
        );

        Collection<BookingDto> sourceBookings = entityBooking.stream().map(mapper::mapToBookingDto)
                .collect(Collectors.toList());

        Collection<BookingDto> targetBookings = service.getAllBookingByUserId(
                1L, State.FUTURE, null, null);

        assertThat(targetBookings, hasSize(sourceBookings.size()));
        for (BookingDto sourceBooking : sourceBookings) {
            assertThat(targetBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(sourceBooking.getStart())),
                    hasProperty("end", equalTo(sourceBooking.getEnd())),
                    hasProperty("status", equalTo(sourceBooking.getStatus()))
            )));
        }
    }

    @Test
    public void shouldGetRejectedBooking() {
        List<Booking> entityBooking = List.of(
                booking3
        );

        Collection<BookingDto> sourceBookings = entityBooking.stream()
                .map(mapper::mapToBookingDto)
                .collect(Collectors.toList());

        Collection<BookingDto> targetBookings = service.getAllBookingByUserId(
                1L, State.FUTURE, null, null);

        assertThat(targetBookings, hasSize(sourceBookings.size()));
        for (BookingDto sourceBooking : sourceBookings) {
            assertThat(targetBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(sourceBooking.getStart())),
                    hasProperty("end", equalTo(sourceBooking.getEnd())),
                    hasProperty("status", equalTo(sourceBooking.getStatus()))
            )));
        }
    }

    @Test
    public void shouldGetPastBooking() {
        List<Booking> entityBooking = List.of(
                booking4
        );

        Collection<BookingDto> sourceBookings = entityBooking.stream().map(mapper::mapToBookingDto)
                .collect(Collectors.toList());

        Collection<BookingDto> targetBookings = service.getAllBookingByUserId(
                1L, State.PAST, null, null);

        assertThat(targetBookings, hasSize(sourceBookings.size()));
    }

    @Test
    public void shouldGetAllBookingWithPagination() {
        List<Booking> entityBooking = List.of(
                booking3
        );

        Collection<BookingDto> sourceBookings = entityBooking.stream().map(mapper::mapToBookingDto).collect(Collectors.toList());

        Collection<BookingDto> targetBookings = service.getAllBookingByUserId(
                1L, State.ALL, 0, 1);

        assertThat(targetBookings, hasSize(sourceBookings.size()));
        for (BookingDto sourceBooking : sourceBookings) {
            assertThat(targetBookings, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("start", equalTo(sourceBooking.getStart())),
                    hasProperty("end", equalTo(sourceBooking.getEnd())),
                    hasProperty("status", equalTo(sourceBooking.getStatus()))
            )));
        }
    }
}
