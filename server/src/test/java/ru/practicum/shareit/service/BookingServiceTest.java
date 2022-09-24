package ru.practicum.shareit.service;

import static ru.practicum.shareit.TestUtil.*;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingCreate;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exeption.*;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository repository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingMapper mapper;
    @InjectMocks
    BookingServiceImpl service;

    @Test
    public void shouldThrowUserNodFoundExceptionInGetAllBooking() {

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.getAllBookingByUserId(1L, State.ALL, 0, 10));

    }

    @Test
    public void shouldGetAllBookingWhenStateEqAll() {

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito
                .when(mapper.mapToBookingDto(Mockito.anyIterable()))
                .thenReturn(new ArrayList<>());

        Mockito
                .when(repository.findAll(Mockito.any(BooleanExpression.class), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        Collection<BookingDto> bookings = service.getAllBookingByUserId(1L, State.ALL, 0, 10);

        assertEquals(0, bookings.size(), "Список всех бронирований пользователя не вернулся.");
    }

    @Test
    public void shouldGetAllBookingWhenStateEqCurrent() {

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito
                .when(mapper.mapToBookingDto(Mockito.anyIterable()))
                .thenReturn(new ArrayList<>());

        Mockito
                .when(repository.findAll(Mockito.any(BooleanExpression.class), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        Collection<BookingDto> bookings = service.getAllBookingByUserId(1L, State.CURRENT, 0, 10);

        assertEquals(0, bookings.size(), "Список всех бронирований пользователя не вернулся.");
    }

    @Test
    public void shouldGetAllBookingWhenStateEqFuture() {

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito
                .when(mapper.mapToBookingDto(Mockito.anyIterable()))
                .thenReturn(new ArrayList<>());

        Mockito
                .when(repository.findAll(Mockito.any(BooleanExpression.class), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        Collection<BookingDto> bookings = service.getAllBookingByUserId(1L, State.FUTURE, 0, 10);

        assertEquals(0, bookings.size(), "Список всех бронирований пользователя не вернулся.");
    }

    @Test
    public void shouldGetAllBookingWhenStateEqPast() {

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito
                .when(mapper.mapToBookingDto(Mockito.anyIterable()))
                .thenReturn(new ArrayList<>());

        Mockito
                .when(repository.findAll(Mockito.any(BooleanExpression.class), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        Collection<BookingDto> bookings = service.getAllBookingByUserId(1L, State.PAST, 0, 10);

        assertEquals(0, bookings.size(), "Список всех бронирований пользователя не вернулся.");
    }

    @Test
    public void shouldGetAllBookingWhenStateEqWaiting() {

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito
                .when(mapper.mapToBookingDto(Mockito.anyIterable()))
                .thenReturn(new ArrayList<>());

        Mockito
                .when(repository.findAll(Mockito.any(BooleanExpression.class), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        Collection<BookingDto> bookings = service.getAllBookingByUserId(1L, State.WAITING, 0, 10);

        assertEquals(0, bookings.size(), "Список всех бронирований пользователя не вернулся.");
    }

    @Test
    public void shouldGetAllBookingWhenStateEqRejected() {

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito
                .when(mapper.mapToBookingDto(Mockito.anyIterable()))
                .thenReturn(new ArrayList<>());

        Mockito
                .when(repository.findAll(Mockito.any(BooleanExpression.class), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        Collection<BookingDto> bookings = service.getAllBookingByUserId(1L, State.REJECTED, 0, 10);

        assertEquals(0, bookings.size(), "Список всех бронирований пользователя не вернулся.");
    }

    @Test
    public void shouldGetAllBookingWithPagination() {

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito
                .when(mapper.mapToBookingDto(Mockito.anyIterable()))
                .thenReturn(List.of(bookingDto1));

        Mockito
                .when(repository.findAll(Mockito.any(BooleanExpression.class), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking1)));

        Collection<BookingDto> bookings = service.getAllBookingByUserId(1L, State.ALL, 0, 1);

        assertEquals(1, bookings.size(), "Список всех бронирований пользователя не вернулся.");
    }

    @Test
    public void shouldThrowItemNodFoundExceptionInGetAllBookedItems() {

        Mockito
                .when(itemRepository.findAllByUserId(Mockito.anyLong()))
                .thenReturn(Collections.emptyList());

        assertThrows(ItemNotFoundException.class,
                () -> service.getAllBookedItemsByUserId(1L, State.ALL, 0, 10));

    }

    @Test
    public void shouldGetAllBookedItemsWhenStateEqAll() {

        Mockito
                .when(itemRepository.findAllByUserId(Mockito.anyLong()))
                .thenReturn(List.of(item1));

        Mockito
                .when(mapper.mapToBookingDto(Mockito.anyIterable()))
                .thenReturn(new ArrayList<>());

        Mockito
                .when(repository.findAll(Mockito.any(BooleanExpression.class), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        Collection<BookingDto> bookings = service.getAllBookedItemsByUserId(
                1L, State.ALL, 0, 10);

        assertEquals(0, bookings.size(), "Список всех бронирований пользователя не вернулся.");
    }

    @Test
    public void shouldSaveNewBooking() {
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito
                .when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item3));

        Mockito
                .when(repository.save(booking1))
                .thenReturn(booking1);

        Mockito
                .when(mapper.mapToBookingCrate(booking1))
                .thenReturn(bookingCreate1);


        BookingCreate booking = service.add(booking1, 1L);

        Mockito
                .verify(repository, Mockito.times(1)).save(booking1);

        assertEquals(1L, booking.getId(), "Бронь не вернулась после сохранения.");
    }

    @Test
    public void shouldThrowUserNodFoundExceptionInAdd() {
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.add(booking1, 1L));
    }

    @Test
    public void shouldThrowItemNodFoundExceptionInAdd() {
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito
                .when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class,
                () -> service.add(booking1, 1L));
    }

    @Test
    public void shouldThrowItemNotAvailableExceptionInAdd() {
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user2));

        Mockito
                .when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item2));

        assertThrows(ItemNotAvailableException.class,
                () -> service.add(booking1, 2L));
    }

    @Test
    public void shouldThrowBookingForbiddenExceptionInAdd() {
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito
                .when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item1));

        assertThrows(BookingForbiddenException.class,
                () -> service.add(booking1, 1L));
    }

    @Test
    public void shouldApproved() {
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking1));
        Mockito
                .when(repository.save(booking1))
                .thenReturn(booking1);

        Mockito
                .when(mapper.mapToBookingDto(booking1))
                .thenReturn(bookingDto1);

        BookingDto booking = service.approved(2L, 1L, true);

        Mockito
                .verify(repository, Mockito.times(1)).save(booking1);

        assertEquals(1L, booking.getId(), "Бронь не вернулась после подтвержедния.");

    }

    @Test
    public void shouldThrowBookingNotFoundExceptionInApproved() {
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class,
                () -> service.approved(1L, 1L, true));
    }

    @Test
    public void shouldThrowUserNotFoundExceptionInApproved() {
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking2));

        assertThrows(UserNotFoundException.class,
                () -> service.approved(2L, 2L, true));
    }

    @Test
    public void shouldThrowIllegalStateExceptionWhenStateEqTrue() {
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking2));

        assertThrows(IllegalStateException.class,
                () -> service.approved(1L, 2L, true));
    }

    @Test
    public void shouldThrowIllegalStateExceptionWhenStateEqFalse() {
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking3));

        assertThrows(IllegalStateException.class,
                () -> service.approved(2L, 3L, false));
    }

    @Test
    public void shouldGetBooking() {
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking1));
        Mockito
                .when(mapper.mapToBookingDto(booking1))
                .thenReturn(bookingDto1);

        BookingDto booking = service.getById(1L, 1L);

        assertEquals(1L, booking.getId(), "Бронь не вернулась.");
    }

    @Test
    public void shouldThrowUserNonFoundExceptionInGetById() {
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(booking1));

        assertThrows(UserNotFoundException.class,
                () -> service.getById(1L, 3L));
    }

}