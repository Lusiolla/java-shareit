package ru.practicum.shareit.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static ru.practicum.shareit.TestUtil.*;

import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exeption.CommentAlreadyExistException;
import ru.practicum.shareit.exeption.CommentForbiddenException;
import ru.practicum.shareit.exeption.ItemNotFoundException;
import ru.practicum.shareit.exeption.UserNotFoundException;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository repository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    BookingMapper bookingMapper;
    @Mock
    CommentMapper commentMapper;
    @Mock
    ItemMapper mapper;
    @InjectMocks
    ItemServiceImpl service;

    @Test
    public void shouldGetItemsWithoutPagination() {
        Collection<Item> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        Mockito
                .when(repository.findAllByUserId(Mockito.anyLong()))
                .thenReturn(items);

        Collection<ItemDto> itemDto = service.getItems(Mockito.anyLong(), null, null);

        assertEquals(2, itemDto.size(), "Список вещей не вернулся.");
    }

    @Test
    public void shouldGetItemsWithPagination() {

        Mockito
                .when(repository.findAllByParam(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(item1));

        Collection<ItemDto> itemDto = service.getItems(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt());

        assertEquals(1, itemDto.size(), "Список вещей не вернулся.");
    }

    @Test
    public void shouldGetItemOfUser() {
        Mockito
                .when(repository.findById(1L))
                .thenReturn(Optional.of(item1));

        Mockito
                .when(mapper.mapToItemWithBooking(item1, bookingShort1, bookingShort1))
                .thenReturn(withBooking1);

        Mockito
                .when(bookingRepository.findNextBooking(Mockito.anyLong(), Mockito.any(LocalDateTime.class)))
                .thenReturn(Optional.of(booking1));

        Mockito
                .when(bookingRepository.findLastBooking(Mockito.anyLong(), Mockito.any(LocalDateTime.class)))
                .thenReturn(Optional.of(booking1));

        Mockito
                .when(bookingMapper.mapToBookingShort(Mockito.any(Booking.class)))
                .thenReturn(bookingShort1);

        ItemDto itemDto = service.getById(1L, 1L);

        assertEquals(1L, itemDto.getId(), "Вещь не вернулась.");
    }

    @Test
    public void shouldGetItemIsNotUser() {

        Mockito
                .when(repository.findById(3L))
                .thenReturn(Optional.of(item3));

        Mockito
                .when(mapper.mapToItemWithBooking(item3, null, null))
                .thenReturn(withBooking2);

        Mockito
                .verify(bookingRepository, Mockito.times(0))
                .findLastBooking(3L, LocalDateTime.now());

        Mockito
                .verify(bookingRepository, Mockito.times(0))
                .findLastBooking(3L, LocalDateTime.now());

        ItemDto itemDto = service.getById(3L, 2L);

        assertEquals(3L, itemDto.getId(), "Вещь не вернулась.");
    }

    @Test
    public void shouldThrowItemNodFoundExceptionInGetById() {

        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> service.getById(1L, 1L));

    }

    @Test
    public void shouldThrowUserNodFoundExceptionInAdd() {

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.add(item1, 1L));
    }

    @Test
    public void shouldSaveNewItem() {
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito
                .when(repository.save(item1))
                .thenReturn(item1);

        Mockito
                .when(mapper.mapToItemDto(item1))
                .thenReturn(withBooking1);


        ItemDto itemDto = service.add(item1, 1L);

        Mockito
                .verify(repository, Mockito.times(1)).save(item1);

        assertEquals(1L, itemDto.getId(), "Вещь не вернулась после сохранения.");
    }

    @Test
    public void shouldThrowItemNodFoundExceptionInAddComment() {

        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> service.addComment(comment1));
    }

    @Test
    public void shouldThrowUserNodFoundExceptionInAddComment() {
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item1));

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> service.addComment(comment1));
    }

    @Test
    public void shouldThrowCommentForbiddenExceptionInAddComment() {

        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item1));

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));
        Mockito
                .when(bookingRepository.findAll(Mockito.any(BooleanExpression.class)))
                .thenReturn(new ArrayList<>());

        assertThrows(CommentForbiddenException.class, () -> service.addComment(comment1));
    }

    @Test
    public void shouldThrowCommentAlreadyExistExceptionInAddComment() {
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item1));

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito
                .when(bookingRepository.findAll(Mockito.any(BooleanExpression.class)))
                .thenReturn(new ArrayList<>(List.of(booking1)));

        Mockito
                .when(commentRepository.save(comment1))
                .thenReturn(comment1);

        Mockito
                .when(commentRepository.save(comment1))
                .thenThrow(RuntimeException.class);

        assertThrows(CommentAlreadyExistException.class, () -> service.addComment(comment1));
    }

    @Test
    public void shouldSaveNewComment() {

        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item1));

        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito
                .when(bookingRepository.findAll(Mockito.any(BooleanExpression.class)))
                .thenReturn(new ArrayList<>(List.of(booking1)));

        Mockito
                .when(commentRepository.save(comment1))
                .thenReturn(comment1);

        Mockito
                .when(commentMapper.mapToCommentDto(comment1))
                .thenReturn(commentDto1);

        CommentDto commentDto = service.addComment(comment1);

        Mockito
                .verify(commentRepository, Mockito.times(1)).save(comment1);

        assertEquals(1L, commentDto.getId(), "Комментарий не вернулся после сохранения.");
    }

    @Test
    public void shouldThrowItemNodFoundExceptionInUpdate() {

        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ItemNotFoundException.class, () -> service.update(itemUpdate1, 1L, 1L));
    }

    @Test
    public void shouldThrowUserNodFoundExceptionInUpdateWhenUserIsNotOwner() {
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item3));

        assertThrows(ItemNotFoundException.class, () -> service.update(itemUpdate1, 1L, 3L));
    }

    @Test
    public void shouldUpdate() {
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item1));
        Mockito
                .when(repository.save(item1))
                .thenReturn(item1);
        Mockito
                .when(mapper.mapToItemDto(item1))
                .thenReturn(itemDtoAfterUpd);

        ItemDto itemDto = service.update(itemUpdate1, 1L, 1L);

        Mockito
                .verify(repository, Mockito.times(1)).save(item1);

        assertEquals(1L, itemDto.getId(), "Вещь не вернулась после обновления.");

    }

    @Test
    public void shouldReturnNewListWhenTextSearchIsBlank() {

        Collection<ItemDto> itemDto = service.search(" ", 0, 10);

        Mockito
                .verify(repository, Mockito.times(0))
                .search(" ", 0, 0);

        assertEquals(0, itemDto.size(), "Вернулся не пустой список.");

    }

    @Test
    public void shouldReturnSearchWithPagination() {

        Collection<ItemDto> itemDto = service.search("text", 0, 10);

        Mockito
                .verify(repository, Mockito.times(1))
                .findAll(Mockito.any(BooleanExpression.class), Mockito.any(PageRequest.class));
        assertEquals(0, itemDto.size(), "Ответ поиска не вернулся.");
    }
}