package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdate;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> getAll();

    UserDto add(User newUser);

    UserDto update(UserUpdate updateUser);

    UserDto getById(long id);

    void delete(long id);
}