package ru.yandex.practicum.shareIt.user;

import java.util.Collection;

public interface UserService {
    Collection<UserResponse> getAll();
    UserResponse add(User newUser);
    UserResponse update(User user);
    UserResponse getById(long id);
    void delete(long id);
}