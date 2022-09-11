package ru.practicum.shareit.service.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {

    private final BookingService service;
    private final BookingMapper mapper;
    private final EntityManager em;

    @Test
    public void shouldGetAllBooking() {

        User user1 = new User(
                null,
                "test6@ya.ru",
                "user1"
        );
        User user2 = new User(null,
                "test7@ya.ru",
                "user2"

        );

        em.persist(user1);
        em.persist(user2);
        em.flush();

        Item item1 = new Item(
                null,
                "test1",
                "description1",
                true,
                user1.getId(),
                null,
                null
        );
        Item item2 = new Item(
                null,
                "test2",
                "description2",
                false,
                user1.getId(),
                null,
                null
        );
        Item item3 = new Item(
                null,
                "test3",
                "description3",
                true,
                user2.getId(),
                null,
                null
        );
        Item item4 = new Item(
                null,
                "test4",
                "description4",
                true,
                user2.getId(),
                null,
                null
        );

        em.persist(item1);
        em.persist(item2);
        em.persist(item3);
        em.persist(item4);
        em.flush();

        Booking booking1 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 6, 15, 56),
                LocalDateTime.of(2022, 9, 7, 15, 56),
                item3,
                user1,
                Status.WAITING
        );
        Booking booking2 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 10, 15, 56),
                LocalDateTime.of(2022, 9, 16, 15, 56),
                item1,
                user2,
                Status.APPROVED
        );
        Booking booking3 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 13, 15, 56),
                LocalDateTime.of(2022, 9, 14, 15, 56),
                item4,
                user1,
                Status.REJECTED
        );
        Booking booking4 = new Booking(
                null,
                LocalDateTime.now(),
                LocalDateTime.of(2022, 9, 14, 15, 56),
                item3,
                user1,
                Status.APPROVED
        );

        em.persist(booking1);
        em.persist(booking2);
        em.persist(booking3);
        em.persist(booking4);
        em.flush();

        List<Booking> entityBooking = List.of(
                booking1,
                booking3,
                booking4
        );

        Collection<BookingDto> sourceBookings = entityBooking.stream().map(mapper::mapToBookingDto)
                .collect(Collectors.toList());

        Collection<BookingDto> targetBookings = service.getAllBookingByUserId(
                user1.getId(), State.ALL, null, null);

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

        User user1 = new User(
                null,
                "test8@ya.ru",
                "user1"
        );
        User user2 = new User(null,
                "test9@ya.ru",
                "user2"

        );

        em.persist(user1);
        em.persist(user2);
        em.flush();

        Item item1 = new Item(
                null,
                "test1",
                "description1",
                true,
                user1.getId(),
                null,
                null
        );
        Item item2 = new Item(
                null,
                "test2",
                "description2",
                false,
                user1.getId(),
                null,
                null
        );
        Item item3 = new Item(
                null,
                "test3",
                "description3",
                true,
                user2.getId(),
                null,
                null
        );
        Item item4 = new Item(
                null,
                "test4",
                "description4",
                true,
                user2.getId(),
                null,
                null
        );

        em.persist(item1);
        em.persist(item2);
        em.persist(item3);
        em.persist(item4);
        em.flush();

        Booking booking1 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 6, 15, 56),
                LocalDateTime.of(2022, 9, 7, 15, 56),
                item3,
                user1,
                Status.WAITING
        );
        Booking booking2 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 10, 15, 56),
                LocalDateTime.of(2022, 9, 16, 15, 56),
                item1,
                user2,
                Status.APPROVED
        );
        Booking booking3 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 13, 15, 56),
                LocalDateTime.of(2022, 9, 14, 15, 56),
                item4,
                user1,
                Status.REJECTED
        );
        Booking booking4 = new Booking(
                null,
                LocalDateTime.now(),
                LocalDateTime.of(2022, 9, 14, 15, 56),
                item3,
                user1,
                Status.APPROVED
        );

        em.persist(booking1);
        em.persist(booking2);
        em.persist(booking3);
        em.persist(booking4);
        em.flush();

        List<Booking> entityBooking = List.of(
                booking4
        );

        Collection<BookingDto> sourceBookings = entityBooking.stream().map(mapper::mapToBookingDto)
                .collect(Collectors.toList());

        Collection<BookingDto> targetBookings = service.getAllBookingByUserId(
                user1.getId(), State.CURRENT, null, null);

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

        User user1 = new User(
                null,
                "test10@ya.ru",
                "user1"
        );
        User user2 = new User(null,
                "test11@ya.ru",
                "user2"

        );
        em.persist(user1);
        em.persist(user2);
        em.flush();

        Item item1 = new Item(
                null,
                "test1",
                "description1",
                true,
                user1.getId(),
                null,
                null
        );
        Item item2 = new Item(
                null,
                "test2",
                "description2",
                false,
                user1.getId(),
                null,
                null
        );

        Item item3 = new Item(
                null,
                "test3",
                "description3",
                true,
                user2.getId(),
                null,
                null
        );
        Item item4 = new Item(
                null,
                "test4",
                "description4",
                true,
                user2.getId(),
                null,
                null
        );
        em.persist(item1);
        em.persist(item2);
        em.persist(item3);
        em.persist(item4);
        em.flush();

        Booking booking1 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 6, 15, 56),
                LocalDateTime.of(2022, 9, 7, 15, 56),
                item3,
                user1,
                Status.WAITING
        );

        Booking booking2 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 10, 15, 56),
                LocalDateTime.of(2022, 9, 16, 15, 56),
                item1,
                user2,
                Status.APPROVED
        );

        Booking booking3 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 13, 15, 56),
                LocalDateTime.of(2022, 9, 14, 15, 56),
                item4,
                user1,
                Status.REJECTED
        );

        Booking booking4 = new Booking(
                null,
                LocalDateTime.now(),
                LocalDateTime.of(2022, 9, 14, 15, 56),
                item3,
                user1,
                Status.APPROVED
        );
        em.persist(booking1);
        em.persist(booking2);
        em.persist(booking3);
        em.persist(booking4);
        em.flush();

        List<Booking> entityBooking = List.of(
                booking3
        );

        Collection<BookingDto> sourceBookings = entityBooking.stream().map(mapper::mapToBookingDto)
                .collect(Collectors.toList());

        Collection<BookingDto> targetBookings = service.getAllBookingByUserId(
                user1.getId(), State.FUTURE, null, null);

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

        User user1 = new User(
                null,
                "test12@ya.ru",
                "user1");
        User user2 = new User(
                null,
                "test13@ya.ru",
                "user2"
        );

        em.persist(user1);
        em.persist(user2);
        em.flush();

        Item item1 = new Item(
                null,
                "test1",
                "description1",
                true,
                user1.getId(),
                null,
                null
        );
        Item item2 = new Item(
                null,
                "test2",
                "description2",
                false,
                user1.getId(),
                null,
                null
        );
        Item item3 = new Item(
                null,
                "test3",
                "description3",
                true,
                user2.getId(),
                null,
                null
        );
        Item item4 = new Item(
                null,
                "test4",
                "description4",
                true,
                user2.getId(),
                null,
                null
        );

        em.persist(item1);
        em.persist(item2);
        em.persist(item3);
        em.persist(item4);
        em.flush();

        Booking booking1 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 6, 15, 56),
                LocalDateTime.of(2022, 9, 7, 15, 56),
                item3,
                user1,
                Status.WAITING
        );
        Booking booking2 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 10, 15, 56),
                LocalDateTime.of(2022, 9, 16, 15, 56),
                item1,
                user2,
                Status.APPROVED
        );
        Booking booking3 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 13, 15, 56),
                LocalDateTime.of(2022, 9, 14, 15, 56),
                item4,
                user1,
                Status.REJECTED
        );
        Booking booking4 = new Booking(
                null,
                LocalDateTime.now(),
                LocalDateTime.of(2022, 9, 14, 15, 56),
                item3,
                user1,
                Status.APPROVED
        );

        em.persist(booking1);
        em.persist(booking2);
        em.persist(booking3);
        em.persist(booking4);
        em.flush();

        List<Booking> entityBooking = List.of(
                booking3
        );

        Collection<BookingDto> sourceBookings = entityBooking.stream()
                .map(mapper::mapToBookingDto)
                .collect(Collectors.toList());

        Collection<BookingDto> targetBookings = service.getAllBookingByUserId(
                user1.getId(), State.FUTURE, null, null);

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

        User user1 = new User(
                null,
                "test14@ya.ru",
                "user1"
        );
        User user2 = new User(null,
                "test15@ya.ru",
                "user2"
        );

        em.persist(user1);
        em.persist(user2);
        em.flush();

        Item item1 = new Item(
                null,
                "test1",
                "description1",
                true,
                user1.getId(),
                null,
                null
        );
        Item item2 = new Item(
                null,
                "test2",
                "description2",
                false,
                user1.getId(),
                null,
                null
        );
        Item item3 = new Item(
                null,
                "test3",
                "description3",
                true,
                user2.getId(),
                null,
                null
        );
        Item item4 = new Item(
                null,
                "test4",
                "description4",
                true,
                user2.getId(),
                null,
                null
        );

        em.persist(item1);
        em.persist(item2);
        em.persist(item3);
        em.persist(item4);
        em.flush();

        Booking booking1 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 6, 15, 56),
                LocalDateTime.of(2022, 9, 7, 15, 56),
                item3,
                user1,
                Status.WAITING
        );
        Booking booking2 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 10, 15, 56),
                LocalDateTime.of(2022, 9, 16, 15, 56),
                item1,
                user2,
                Status.APPROVED
        );
        Booking booking3 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 13, 15, 56),
                LocalDateTime.of(2022, 9, 14, 15, 56),
                item4,
                user1,
                Status.REJECTED
        );
        Booking booking4 = new Booking(
                null,
                LocalDateTime.now(),
                LocalDateTime.of(2022, 9, 14, 15, 56),
                item3,
                user1,
                Status.APPROVED
        );

        em.persist(booking1);
        em.persist(booking2);
        em.persist(booking3);
        em.persist(booking4);
        em.flush();

        List<Booking> entityBooking = List.of(
                booking4
        );

        Collection<BookingDto> sourceBookings = entityBooking.stream().map(mapper::mapToBookingDto)
                .collect(Collectors.toList());

        Collection<BookingDto> targetBookings = service.getAllBookingByUserId(
                user1.getId(), State.PAST, null, null);

        assertThat(targetBookings, hasSize(sourceBookings.size()));
    }

    @Test
    public void shouldGetAllBookingWithPagination() {

        User user1 = new User(
                null,
                "test16@ya.ru",
                "user1"
        );

        User user2 = new User(null,
                "test17@ya.ru",
                "user2"
        );

        em.persist(user1);
        em.persist(user2);
        em.flush();

        Item item1 = new Item(
                null,
                "test1",
                "description1",
                true,
                user1.getId(),
                null,
                null
        );
        Item item2 = new Item(
                null,
                "test2",
                "description2",
                false,
                user1.getId(),
                null,
                null
        );
        Item item3 = new Item(
                null,
                "test3",
                "description3",
                true,
                user2.getId(),
                null,
                null
        );
        Item item4 = new Item(
                null,
                "test4",
                "description4",
                true,
                user2.getId(),
                null,
                null
        );

        em.persist(item1);
        em.persist(item2);
        em.persist(item3);
        em.persist(item4);
        em.flush();

        Booking booking1 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 6, 15, 56),
                LocalDateTime.of(2022, 9, 7, 15, 56),
                item3,
                user1,
                Status.WAITING
        );
        Booking booking2 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 10, 15, 56),
                LocalDateTime.of(2022, 9, 16, 15, 56),
                item1,
                user2,
                Status.APPROVED
        );
        Booking booking3 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 13, 15, 56),
                LocalDateTime.of(2022, 9, 14, 15, 56),
                item4,
                user1,
                Status.REJECTED
        );
        Booking booking4 = new Booking(
                null,
                LocalDateTime.now(),
                LocalDateTime.of(2022, 9, 14, 15, 56),
                item3,
                user1,
                Status.APPROVED
        );

        em.persist(booking1);
        em.persist(booking2);
        em.persist(booking3);
        em.persist(booking4);
        em.flush();

        List<Booking> entityBooking = List.of(
                booking3
        );

        Collection<BookingDto> sourceBookings = entityBooking.stream().map(mapper::mapToBookingDto).collect(Collectors.toList());

        Collection<BookingDto> targetBookings = service.getAllBookingByUserId(
                user1.getId(), State.ALL, 0, 1);

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
