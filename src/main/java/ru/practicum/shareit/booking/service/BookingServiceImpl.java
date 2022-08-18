package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingCreate;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exeption.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper mapper;

    @Override
    public Collection<BookingDto> getAllBookingByUserId(long userId, State state) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        switch (state) {
            case ALL:
                return bookingRepository.findAllByUserIdOrderByStartBookingDesc(userId)
                        .stream()
                        .map(mapper::mapToBookingDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository
                        .findAllByUserIdAndStartBookingLessThanEqualAndEndBookingAfterOrderByStartBookingDesc(
                                userId, LocalDateTime.now(), LocalDateTime.now())
                        .stream()
                        .map(mapper::mapToBookingDto).collect(Collectors.toList());
            case PAST:
                return bookingRepository.findAllByUserIdAndEndBookingBeforeOrderByStartBookingDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(mapper::mapToBookingDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findAllByUserIdAndStartBookingAfterOrderByStartBookingDesc(userId, LocalDateTime.now())
                        .stream()
                        .map(mapper::mapToBookingDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findAllByUserIdAndStatusOrderByStartBookingDesc(userId, Status.WAITING)
                        .stream()
                        .map(mapper::mapToBookingDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findAllByUserIdAndStatusOrderByStartBookingDesc(userId, Status.REJECTED)
                        .stream()
                        .map(mapper::mapToBookingDto)
                        .collect(Collectors.toList());
            default:
                throw new StatusNotSupportedException();

        }
    }

    @Override
    public Collection<BookingDto> getAllBookingItemsByUserId(long userId, State state) {
        Collection<Item> items = itemRepository.findAllByUserId(userId);
        if (items.isEmpty()) {
            throw new ItemNotFoundException();
        }
        Collection<Long> itemId = items.stream().map(Item::getId).collect(Collectors.toList());
        switch (state) {
            case ALL:
                return bookingRepository.findAllByItemIdInOrderByStartBookingDesc(itemId)
                        .stream()
                        .map(mapper::mapToBookingDto)
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository
                        .findAllByItemIdInAndStartBookingBeforeAndEndBookingGreaterThanEqualOrderByStartBookingDesc(
                                itemId, LocalDateTime.now(), LocalDateTime.now())
                        .stream()
                        .map(mapper::mapToBookingDto).collect(Collectors.toList());
            case PAST:
                return bookingRepository.findAllByItemIdInAndEndBookingBeforeOrderByStartBookingDesc(
                                itemId, LocalDateTime.now())
                        .stream()
                        .map(mapper::mapToBookingDto)
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findAllByItemIdInAndStartBookingAfterOrderByStartBookingDesc(
                                itemId, LocalDateTime.now())
                        .stream()
                        .map(mapper::mapToBookingDto)
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findAllByItemIdInAndStatusOrderByStartBookingDesc(itemId, Status.WAITING)
                        .stream()
                        .map(mapper::mapToBookingDto)
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findAllByItemIdInAndStatusOrderByStartBookingDesc(itemId, Status.REJECTED)
                        .stream()
                        .map(mapper::mapToBookingDto)
                        .collect(Collectors.toList());
            default:
                throw new StatusNotSupportedException();
        }
    }

    @Override
    @Transactional
    public BookingCreate add(Booking newBooking, long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Item item = itemRepository.findById(newBooking.getItem().getId()).orElseThrow(ItemNotFoundException::new);
        if (!item.getAvailable()) {
            throw new ItemNotAvailableException();
        }
        if (item.getUserId() == userId) {
            throw new UserNotFoundException();
        }

        Collection<Booking> bookings = bookingRepository.findIntersectionsByItemId(newBooking.getItem().getId(),
                newBooking.getStartBooking(), newBooking.getEndBooking());
        if (!bookings.isEmpty()) {
            throw new BookingAlreadyExistException();
        }
        newBooking.setUser(user);
        return mapper.mapToBookingCrate(bookingRepository.save(newBooking));

    }

    @Override
    @Transactional
    public BookingDto approved(long userId, long bookingId, boolean isApproved) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(BookingNotFoundException::new);

        if (booking.getItem().getUserId() != userId) {
            throw new UserNotFoundException();
        }

        if (isApproved) {
            if (booking.getStatus() == Status.APPROVED) {
                throw new IllegalStateException();
            }
            booking.setStatus(Status.APPROVED);
        } else {
            if (booking.getStatus() == Status.REJECTED) {
                throw new IllegalStateException();
            }
            booking.setStatus(Status.REJECTED);
        }
        return mapper.mapToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getById(long id, long userId) {
        Booking booking = bookingRepository.findById(id).orElseThrow(BookingNotFoundException::new);
        if (booking.getUser().getId() != userId && booking.getItem().getUserId() != userId) {
            throw new UserNotFoundException();
        }
        return mapper.mapToBookingDto(booking);
    }
}
