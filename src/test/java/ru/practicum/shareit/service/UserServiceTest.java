package ru.practicum.shareit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exeption.UserAlreadyExistException;
import ru.practicum.shareit.exeption.UserNotFoundException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static ru.practicum.shareit.TestUtil.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;
    @InjectMocks
    UserServiceImpl service;

    @Test
    public void shouldUpdate() {
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito
                .when(repository.save(user1AfterUpd))
                .thenReturn(user1AfterUpd);

        Mockito
                .when(mapper.mapToUserDto(user1AfterUpd))
                .thenReturn(userDtoAfterUpd);


        UserDto user = service.update(userUpdate1);

        Mockito
                .verify(repository, Mockito.times(1))
                .save(user1AfterUpd);

        assertEquals("user1upd", user.getName(), "Пользователь не вернулся после обновления.");
    }

    @Test
    public void shouldThrowUserNonFoundExceptionInUpdate() {
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.getById(1L));
    }

    @Test
    public void shouldAddNewUser() {
        Mockito
                .when(repository.save(user1))
                .thenReturn(user1);

        Mockito
                .when(mapper.mapToUserDto(user1))
                .thenReturn(userDto1);

        UserDto user = service.add(user1);

        Mockito
                .verify(repository, Mockito.times(1))
                .save(user1);

        assertEquals(1L, user.getId(), "Пользователь не вернулся после сохранения.");
    }

    @Test
    public void shouldThrowUserAlreadyExistException() {
        Mockito
                .when(mapper.mapToUserDto(user1))
                .thenReturn(userDto1);

        assertThrows(UserAlreadyExistException.class,
                () -> service.add(user1));
    }

    @Test
    public void shouldGetUser() {
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        Mockito
                .when(mapper.mapToUserDto(user1))
                .thenReturn(userDto1);

        UserDto user = service.getById(1L);

        assertEquals(1L, user.getId(), "Пользователь не был найден.");
    }

    @Test
    public void shouldThrowUserNotFoundException() {
        Mockito
                .when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.getById(1L));
    }

    @Test
    public void shouldDeleteUser() {
        repository.deleteById(1L);

        Mockito
                .verify(repository, Mockito.times(1))
                .deleteById(1L);
    }

    @Test
    public void shouldGetAll() {
        Mockito
                .when(repository.findAll())
                .thenReturn(List.of(user1));

        Mockito
                .when(mapper.mapToUserDto(user1))
                .thenReturn(userDto1);

        Collection<UserDto> users = service.getAll();

        assertEquals(1, users.size(), "Список пользователей не вернулся.");
    }
}