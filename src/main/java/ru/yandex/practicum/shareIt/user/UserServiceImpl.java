package ru.yandex.practicum.shareIt.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public List<UserResponse> getAll() {

        return repository.findAll().stream().map(mapper::mapToUserResponse).collect(Collectors.toList());
    }

    @Override
    public UserResponse add(User newUser) {

        return mapper.mapToUserResponse(repository.saveNew(newUser));
    }

    @Override
    public UserResponse update(User user) {

        return mapper.mapToUserResponse(repository.update(mapper.mapToUserUpdate(user)));
    }

    @Override
    public UserResponse getById(long id) {

        return mapper.mapToUserResponse(repository.findById(id));
    }

    @Override
    public void delete(long id) {

        repository.delete(id);
    }
}