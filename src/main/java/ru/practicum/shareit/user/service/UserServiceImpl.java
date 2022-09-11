package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.UserAlreadyExistException;
import ru.practicum.shareit.exeption.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdate;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public List<UserDto> getAll() {

        return repository.findAll().stream().map(mapper::mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto add(User newUser) {
        try {
            return mapper.mapToUserDto(repository.save(newUser));
        } catch (RuntimeException e) {
            throw new UserAlreadyExistException();
        }

    }

    @Override
    @Transactional
    public UserDto update(UserUpdate updateUser) {
        User userFromRepository = repository.findById(updateUser.getId()).orElseThrow(UserNotFoundException::new);

        if (updateUser.getEmail().isPresent()) {
            userFromRepository.setEmail(updateUser.getEmail().get());
        }

        if (updateUser.getName().isPresent()) {
            userFromRepository.setName(updateUser.getName().get());
        }
        try {
            return mapper.mapToUserDto(repository.save(userFromRepository));
        } catch (RuntimeException e) {
            throw new UserAlreadyExistException();
        }

    }

    @Override
    public UserDto getById(long id) {

        return mapper.mapToUserDto(repository.findById(id).orElseThrow(UserNotFoundException::new));
    }

    @Override
    public void delete(long id) {

        repository.deleteById(id);
    }

}