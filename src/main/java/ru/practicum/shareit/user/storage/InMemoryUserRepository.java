package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exeption.UserAlreadyExistException;
import ru.practicum.shareit.exeption.UserNotFoundException;
import ru.practicum.shareit.item.srorage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserUpdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();
    private final ItemRepository itemRepository;

    private long id;

    @Override
    public List<User> findAll() {
        List<User> listUsers = new ArrayList<>();
        if (!users.isEmpty()) {
            users.forEach((key, value) -> listUsers.add(value));
        }
        return listUsers;
    }

    @Override
    public User saveNew(User newUser) {
        checkEmail(newUser.getEmail());
        id = id + 1;
        newUser.setId(id);
        users.put(newUser.getId(), newUser);
        itemRepository.saveNewUser(newUser.getId());
        return newUser;
    }

    @Override
    public User update(UserUpdate updateUser) {
        User userFromRepository = users.get(updateUser.getId());
        if (userFromRepository == null) {
            throw new UserNotFoundException();
        }

        if (updateUser.getEmail().isPresent()) {
            checkEmail(updateUser.getEmail().get());
            userFromRepository.setEmail(updateUser.getEmail().get());
        }

        if (updateUser.getName().isPresent()) {
            userFromRepository.setName(updateUser.getName().get());
        }

        return userFromRepository;
    }

    @Override
    public User findById(long id) {
        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException();
        } else {
            return user;
        }
    }

    @Override
    public void delete(long id) {
        users.remove(id);
        itemRepository.deleteByUserId(id);
    }

    private void checkEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {
                throw new UserAlreadyExistException();
            }
        }
    }
}
