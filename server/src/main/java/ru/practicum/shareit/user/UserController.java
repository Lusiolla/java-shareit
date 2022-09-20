package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdate;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    @GetMapping
    public Collection<UserDto> getAll() {
        return userService.getAll();
    }

    @GetMapping("{id}")
    public UserDto getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping
    public UserDto add(@RequestBody UserDto newUser) {
        return userService.add(mapper.mapToUser(newUser));
    }

    @PatchMapping("{id}")
    public UserDto update(@PathVariable Long id,
                          @RequestBody UserUpdate updateUser) {
        updateUser.setId(id);
        return userService.update(updateUser);
    }

    @DeleteMapping("{id}")
    public void delete(@NotNull @PathVariable Long id) {
        userService.delete(id);
    }
}
