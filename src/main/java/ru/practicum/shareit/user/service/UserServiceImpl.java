package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserUpdate;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public List<UserDTO> getAll() {

        return repository.findAll().stream().map(mapper::mapToUserResponse).collect(Collectors.toList());
    }

    @Override
    public UserDTO add(User newUser) {

        return mapper.mapToUserResponse(repository.saveNew(newUser));
    }

    @Override
    public UserDTO update(UserUpdate updateUser) {
        return mapper.mapToUserResponse(repository.update(updateUser));
    }

    @Override
    public UserDTO getById(long id) {

        return mapper.mapToUserResponse(repository.findById(id));
    }

    @Override
    public void delete(long id) {

        repository.delete(id);
    }
}