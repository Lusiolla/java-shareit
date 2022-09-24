package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.exeption.*;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdate;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.QItem;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@Component
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper mapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;

    @Override
    public Collection<ItemDto> getItems(long userId, Integer from, Integer size) {
        if (from == null || size == null) {
            return repository.findAllByUserId(userId)
                    .stream().sorted(Comparator.comparing(Item::getId))
                    .map(item -> mapper.mapToItemWithBooking(item,
                            bookingMapper.mapToBookingShort(bookingRepository
                                    .findLastBooking(item.getId(), LocalDateTime.now()).orElse(null)),
                            bookingMapper.mapToBookingShort(bookingRepository
                                    .findNextBooking(item.getId(), LocalDateTime.now()).orElse(null))))
                    .collect(Collectors.toList());
        } else {
            return repository.findAllByParam(userId, size, from)
                    .stream().sorted(Comparator.comparing(Item::getId))
                    .map(item -> mapper.mapToItemWithBooking(item,
                            bookingMapper.mapToBookingShort(bookingRepository
                                    .findLastBooking(item.getId(), LocalDateTime.now()).orElse(null)),
                            bookingMapper.mapToBookingShort(bookingRepository
                                    .findNextBooking(item.getId(), LocalDateTime.now()).orElse(null))))
                    .collect(Collectors.toList());
        }

    }

    @Override
    public ItemDto getById(long id, long userId) {
        Item item = repository.findById(id).orElseThrow(ItemNotFoundException::new);
        if (item.getUserId() == userId) {
            return mapper.mapToItemWithBooking(item,
                    bookingMapper.mapToBookingShort(bookingRepository.findLastBooking(id, LocalDateTime.now())
                            .orElse(null)),
                    bookingMapper.mapToBookingShort(bookingRepository.findNextBooking(id, LocalDateTime.now())
                            .orElse(null)));
        } else {
            return mapper.mapToItemWithBooking(item, null, null);
        }
    }

    @Override
    @Transactional
    public ItemDto add(Item item, long userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        item.setUserId(userId);

        return mapper.mapToItemDto(repository.save(item));
    }

    @Override
    @Transactional
    public CommentDto addComment(Comment comment) {
        repository.findById(comment.getItemId()).orElseThrow(ItemNotFoundException::new);
        User user = userRepository.findById(comment.getUser().getId()).orElseThrow(UserNotFoundException::new);
        Iterable<Booking> bookings = bookingRepository.findAll(QBooking.booking.user.id.eq(comment.getUser().getId())
                .and(QBooking.booking.item.id.eq(comment.getItemId())
                        .and(QBooking.booking.startBooking.before(LocalDateTime.now()))
                        .and(QBooking.booking.status.eq(Status.APPROVED))));

        if (!bookings.iterator().hasNext()) {
            throw new CommentForbiddenException();
        }
        comment.setUser(user);
        comment.setCreated(LocalDateTime.now());
        try {
            return commentMapper.mapToCommentDto(commentRepository.save(comment));
        } catch (RuntimeException e) {
            throw new CommentAlreadyExistException();
        }

    }

    @Override
    @Transactional
    public ItemDto update(ItemUpdate updateItem, long userId, long itemId) {
        Item itemFromRepository = repository.findById(itemId).orElseThrow(ItemNotFoundException::new);
        if (itemFromRepository.getUserId() != userId) {
            throw new ItemNotFoundException();
        }

        if (updateItem.getName().isPresent()) {
            itemFromRepository.setName(updateItem.getName().get());
        }

        if (updateItem.getDescription().isPresent()) {
            itemFromRepository.setDescription(updateItem.getDescription().get());
        }

        if (updateItem.getAvailable().isPresent()) {
            itemFromRepository.setAvailable(updateItem.getAvailable().get());
        }
        return mapper.mapToItemDto(repository.save(itemFromRepository));
    }

    @Override
    public void delete(long userId, long itemId) {
        repository.deleteByIdAndUserId(itemId, userId);
    }

    @Override
    public Collection<ItemDto> search(String text, Integer from, Integer size) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }

        return mapper.mapToItemDto(repository.findAll(
                QItem.item.name.upper().contains(text.toUpperCase())
                        .or(QItem.item.description.upper().contains(text.toUpperCase())
                                .and(QItem.item.available.eq(true))),
                PageRequest.of(from / size, size)
        ));
    }
}