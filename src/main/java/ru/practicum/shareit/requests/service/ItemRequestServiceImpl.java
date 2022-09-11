package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.ItemRequestNotFoundException;
import ru.practicum.shareit.exeption.UserNotFoundException;
import ru.practicum.shareit.requests.ItemRequestMapper;
import ru.practicum.shareit.requests.ItemRequestRepository;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    private final UserRepository userRepository;
    private final ItemRequestMapper mapper;

    @Override
    @Transactional
    public ItemRequestDto add(ItemRequest itemRequest) {
        userRepository.findById(itemRequest.getUserId()).orElseThrow(UserNotFoundException::new);
        itemRequest.setCreated(LocalDateTime.now());
        return mapper.mapToRequestDto(repository.save(itemRequest));
    }

    @Override
    public Collection<ItemRequestDto> getAllByUserId(long userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return repository.findAllByUserIdOrderByCreatedDesc(userId)
                .stream()
                .map(mapper::mapToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemRequestDto> getAll(long userId, Integer from, Integer size) {
        if (from == null || size == null) {
            return repository.findAllByUserIdNotOrderByCreatedDesc(userId)
                    .stream()
                    .map(mapper::mapToRequestDto)
                    .collect(Collectors.toList());
        } else {
            return repository.findAllByParam(userId, size, from)
                    .stream()
                    .map(mapper::mapToRequestDto)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public ItemRequestDto getById(long userId, long id) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return mapper.mapToRequestDto(repository.findById(id).orElseThrow(ItemRequestNotFoundException::new));
    }
}
