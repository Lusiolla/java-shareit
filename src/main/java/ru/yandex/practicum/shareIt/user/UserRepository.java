package ru.yandex.practicum.shareIt.user;

import java.util.Collection;

public interface UserRepository {
    Collection<User> findAll();
    User saveNew(User newUser);
    User update(UserUpdate updateUser);
    User findById(long id);
    void delete(long id);
}