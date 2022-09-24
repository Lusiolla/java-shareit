package ru.practicum.shareit;

import lombok.Data;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingCreate;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.item.dto.ItemWithBooking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdate;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Data
public abstract class TestUtil {

    public static User user1 = new User(
            1L,
            "test1@ya.ru",
            "user1"

    );

    public static User user2 = new User(
            2L,
            "test2@ya.ru",
            "user2"

    );

    public static UserUpdate userUpdate1 = new UserUpdate(
            1L,
            Optional.empty(),
            Optional.of("user1upd")
    );

    public static User user1AfterUpd = new User(
            1L,
            "test1@ya.ru",
            "user1upd"
    );

    public static UserDto userDtoAfterUpd = new UserDto(
            1L,
            "test1@ya.ru",
            "user1upd"

    );

    public static UserDto userDto1 = new UserDto(
            1L,
            "test1@ya.ru",
            "user1"

    );
    public static Comment comment1 = new Comment(
            1L,
            "good",
            1L,
            user2,
            null
    );

    public static CommentDto commentDto1 = new CommentDto(
            1L,
            "good",
            "name1",
            null
    );
    public static Item item1 = new Item(
            1L,
            "test1",
            "description1",
            true,
            1L,
            null,
            null
    );
    public static Item item2 = new Item(
            2L,
            "test2",
            "description2",
            false,
            1L,
            null,
            null
    );

    public static Item item3 = new Item(
            3L,
            "test3",
            "description3",
            true,
            2L,
            null,
            null
    );

    public static Item item4 = new Item(
            4L,
            "test4",
            "description4",
            true,
            2L,
            null,
            null
    );

    public static ItemUpdate itemUpdate1 = new ItemUpdate(
            1L,
            Optional.of("name1"),
            Optional.empty(),
            Optional.empty(),
            1L
    );

    public static ItemUpdate itemUpdate2 = new ItemUpdate(
            1L,
            Optional.empty(),
            Optional.of("description1"),
            Optional.empty(),
            1L
    );

    public static ItemUpdate itemUpdate3 = new ItemUpdate(
            1L,
            Optional.empty(),
            Optional.empty(),
            Optional.of(false),
            1L
    );

    public static Booking booking1 = new Booking(
            1L,
            LocalDateTime.of(2022, 9, 6, 15, 56),
            LocalDateTime.of(2022, 9, 7, 15, 56),
            item3,
            user1,
            Status.WAITING
    );

    public static Booking booking2 = new Booking(
            2L,
            LocalDateTime.of(2022, 9, 10, 15, 56),
            LocalDateTime.of(2022, 9, 16, 15, 56),
            item1,
            user2,
            Status.APPROVED
    );

    public static Booking booking3 = new Booking(
            3L,
            LocalDateTime.of(2022, 9, 13, 15, 56),
            LocalDateTime.of(2022, 9, 14, 15, 56),
            item4,
            user1,
            Status.REJECTED
    );

    public static Booking booking4 = new Booking(
            4L,
            LocalDateTime.now(),
            LocalDateTime.of(2022, 9, 14, 15, 56),
            item3,
            user1,
            Status.APPROVED
    );

    public static BookingDto bookingDto1 = new BookingDto(
            1L,
            LocalDateTime.of(2022, 9, 6, 15, 56),
            LocalDateTime.of(2022, 9, 7, 15, 56),
            user1,
            item3,
            Status.WAITING

    );

    public static BookingDto bookingDto2 = new BookingDto(
            2L,
            LocalDateTime.of(2022, 9, 15, 15, 56),
            LocalDateTime.of(2022, 9, 16, 15, 56),
            user2,
            item1,
            Status.APPROVED

    );

    public static BookingCreate bookingCreate1 = new BookingCreate(
            1L,
            LocalDateTime.of(2022, 9, 20, 13, 44),
            LocalDateTime.of(2022, 9, 21, 13, 44),
            3L,
            1L,
            Status.WAITING
    );


    public static BookingShort bookingShort1 = new BookingShort(
            1L,
            LocalDateTime.of(2022, 9, 17, 15, 56),
            LocalDateTime.of(2022, 9, 18, 15, 56),
            1L
    );
    public static BookingShort bookingShort2 = new BookingShort(
            2L,
            null,
            null,
            null
    );


    public static ItemDto itemDtoAfterUpd = new ItemWithBooking(
            1L,
            "name1",
            "description",
            true,
            null,
            null,
            null,
            null
    );

    public static ItemDto itemDto1 = new ItemWithBooking(
            1L,
            "name1",
            "description1",
            true,
            null,
            null,
            null,
            bookingShort1
    );

    public static ItemDto itemDto2 = new ItemWithBooking(
            2L,
            "name2",
            "description2",
            true,
            null,
            null,
            null,
            null
    );

    public static ItemDto itemDto3 = new ItemWithBooking(
            3L,
            "name3",
            "description3",
            true,
            null,
            null,
            null,
            null
    );


    public static ItemWithBooking withBooking1 = new ItemWithBooking(
            1L,
            "test1",
            "description",
            true,
            null,
            List.of(commentDto1),
            bookingShort1,
            bookingShort2
    );

    public static ItemWithBooking withBooking2 = new ItemWithBooking(
            3L,
            "test1",
            "description",
            true,
            null,
            null,
            null,
            null
    );

    public static ItemRequest itemRequest1 = new ItemRequest(
            1L,
            "description1",
            1L,
            null,
            null
    );

    public static ItemRequest itemRequest2 = new ItemRequest(
            2L,
            "description2",
            2L,
            null,
            null
    );

    public static ItemRequestDto itemRequestDto1 = new ItemRequestDto(
            1L,
            "description",
            null,
            null

    );

    public static ItemRequestDto itemRequestDto2 = new ItemRequestDto(
            2L,
            "description2",
            null,
            null

    );
}
