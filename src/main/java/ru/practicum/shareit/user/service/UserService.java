package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserUpdate;

import java.util.Collection;

public interface UserService {
    Collection<UserDTO> getAll();

    UserDTO add(User newUser);

    UserDTO update(UserUpdate updateUser);

    UserDTO getById(long id);

    void delete(long id);
}