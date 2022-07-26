package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserUpdate;

import java.util.Collection;

public interface UserRepository {
    Collection<User> findAll();

    User saveNew(User newUser);

    User update(UserUpdate updateUser);

    User findById(long id);

    void delete(long id);
}