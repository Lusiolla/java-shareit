package ru.practicum.shareit.booking.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import ru.practicum.shareit.booking.model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository repository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingMapper mapper;

    @Override
    public Collection<BookingDto> getAllBookingByUserId(long userId, State state, Integer from, Integer size) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Collection<BooleanExpression> conditions = new ArrayList<>();
        conditions.add(QBooking.booking.user.id.eq(userId));

        if (!state.equals(State.ALL)) {
            conditions.add(makeStateCondition(state));
        }

        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();
        return mapper.mapToBookingDto(makePageRequest(finalCondition, from, size));
    }

    @Override
    public Collection<BookingDto> getAllBookedItemsByUserId(long userId, State state, Integer from, Integer size) {
        Collection<Item> items = itemRepository.findAllByUserId(userId);
        if (items.isEmpty()) {
            throw new ItemNotFoundException();
        }

        Collection<BooleanExpression> conditions = new ArrayList<>();
        conditions.add(QBooking.booking.item.id.in(items.stream()
                .map(Item::getId)
                .collect(Collectors.toList())));

        if (!state.equals(State.ALL)) {
            conditions.add(makeStateCondition(state));
        }

        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        return mapper.mapToBookingDto(makePageRequest(finalCondition, from, size));
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
            throw new BookingForbiddenException();
        }
        newBooking.setUser(user);

        return mapper.mapToBookingCrate(repository.save(newBooking));
    }

    @Override
    @Transactional
    public BookingDto approved(long userId, long bookingId, boolean isApproved) {
        Booking booking = repository.findById(bookingId).orElseThrow(BookingNotFoundException::new);

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

        return mapper.mapToBookingDto(repository.save(booking));
    }

    @Override
    public BookingDto getById(long id, long userId) {
        Booking booking = repository.findById(id).orElseThrow(BookingNotFoundException::new);
        if (booking.getUser().getId() != userId && booking.getItem().getUserId() != userId) {
            throw new UserNotFoundException();
        }

        return mapper.mapToBookingDto(booking);
    }

    private BooleanExpression makeStateCondition(State state) {
        switch (state) {
            case CURRENT:
                return QBooking.booking.startBooking.loe(LocalDateTime.now())
                        .and(QBooking.booking.endBooking.after(LocalDateTime.now()));
            case PAST:
                return QBooking.booking.endBooking.before(LocalDateTime.now());
            case FUTURE:
                return QBooking.booking.startBooking.after(LocalDateTime.now());
            case WAITING:
                return QBooking.booking.status.eq(Status.WAITING);
            case REJECTED:
                return QBooking.booking.status.eq(Status.REJECTED);
            default:
                throw new RuntimeException();

        }
    }

    private Iterable<Booking> makePageRequest(BooleanExpression finalCondition, Integer from, Integer size) {
        Sort sort = Sort.by("startBooking").descending();

        if (from == null || size == null) {
            return repository.findAll(finalCondition, sort);
        } else {
            return repository.findAll(finalCondition, PageRequest.of(from / size, size, sort));
        }
    }

}
