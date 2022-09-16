package ru.practicum.shareit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exeption.ItemRequestNotFoundException;
import ru.practicum.shareit.exeption.UserNotFoundException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.requests.ItemRequestMapper;
import ru.practicum.shareit.requests.ItemRequestRepository;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.TestUtil.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    @Mock
    private ItemRequestRepository repository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRequestMapper mapper;
    @Mock
    private ItemMapper itemMapper;
    @InjectMocks
    ItemRequestServiceImpl service;

    @Test
    public void shouldSaveNewItemRequest() {
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito
                .when(repository.save(itemRequest1))
                .thenReturn(itemRequest1);

        Mockito
                .when(mapper.mapToRequestDto(itemRequest1))
                .thenReturn(itemRequestDto1);

        Mockito
                .when(mapper.mapToRequestDto(itemRequest1))
                .thenReturn(itemRequestDto1);

        ItemRequestDto itemRequest = service.add(itemRequest1);

        Mockito
                .verify(repository, Mockito.times(1)).save(itemRequest1);

        assertEquals(1L, itemRequest.getId(), "Запрос не вернулась после сохранения.");
    }

    @Test
    public void shouldThrowUserNodFoundExceptionInAdd() {
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.add(itemRequest1));
    }

    @Test
    public void shouldGetAllByUser() {
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito
                .when(mapper.mapToRequestDto(itemRequest1))
                .thenReturn(itemRequestDto1);

        Mockito
                .when(repository.findAllByUserIdOrderByCreatedDesc(Mockito.anyLong()))
                .thenReturn(List.of(itemRequest1));

        Collection<ItemRequestDto> itemRequests = service.getAllByUserId(1L);

        assertEquals(1, itemRequests.size(), "Список запросов не вернулся.");
    }


    @Test
    public void shouldThrowUserNodFoundExceptionInGetAllByUserId() {
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.getAllByUserId(1L));
    }

    @Test
    public void shouldGetAllWithoutPagination() {

        Mockito
                .when(mapper.mapToRequestDto(itemRequest2))
                .thenReturn(itemRequestDto2);

        Mockito
                .when(repository.findAllByUserIdNotOrderByCreatedDesc(1L))
                .thenReturn(List.of(itemRequest2));

        Collection<ItemRequestDto> itemRequests = service.getAll(1L, null, null);

        Mockito
                .verify(repository, Mockito.times(0))
                .findAllByParam(1L, 0, 0);

        assertEquals(1, itemRequests.size(), "Список запросов не вернулся.");
    }

    @Test
    public void shouldGetAllWithPagination() {

        Mockito
                .when(mapper.mapToRequestDto(itemRequest2))
                .thenReturn(itemRequestDto2);

        Mockito
                .when(repository.findAllByParam(1L, 1, 0))
                .thenReturn(List.of(itemRequest2));

        Collection<ItemRequestDto> itemRequests = service.getAll(1L, 0, 1);

        Mockito
                .verify(repository, Mockito.times(0))
                .findAllByUserIdNotOrderByCreatedDesc(1L);

        assertEquals(1, itemRequests.size(), "Список запросов не вернулся.");
    }

    @Test
    public void shouldGetItemRequest() {
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(itemRequest2));

        Mockito
                .when(mapper.mapToRequestDto(itemRequest2))
                .thenReturn(itemRequestDto2);

        ItemRequestDto itemRequest = service.getById(1L, 2L);

        assertEquals(2L, itemRequest.getId(), "Запрос не был найден.");
    }

    @Test
    public void shouldThrowUserNodFoundExceptionInGetById() {
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.getById(1L, 2L));
    }

    @Test
    public void shouldThrowItemRequestNodFoundExceptionInGetById() {
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(ItemRequestNotFoundException.class,
                () -> service.getById(1L, 2L));
    }
}