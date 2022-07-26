package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserUpdate;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserMapper mapper;

    @GetMapping
    public Collection<UserDTO> getAll() {
        Collection<UserDTO> response = userService.getAll();
        log.debug("Returned the list of users: {}", response.size());
        return response;
    }

    @GetMapping("{id}")
    public UserDTO getById(@Valid @NotNull @PathVariable Long id) {
        UserDTO user = userService.getById(id);
        log.debug("Returned the user: {}", user.getId());
        return user;
    }

    @PostMapping
    public UserDTO add(@Valid @NotNull @RequestBody UserDTO newUser) {
        UserDTO user = userService.add(mapper.mapToUser(newUser));
        log.debug("User added: {}", user.getId());
        return user;
    }

    @PatchMapping("{id}")
    public UserDTO update(@RequestBody Map<String, Object> updates,
                          @Valid @NotNull @PathVariable Long id) {
        UserUpdate updateUser = mapper.mapToUserUpdate(updates);
        updateUser.setId(id);
        UserDTO user = userService.update(updateUser);
        log.debug("Updated user information: {}", user.getId());
        return user;
    }

    @DeleteMapping("{id}")
    public void delete(@Valid @NotNull @PathVariable Long id) {
        userService.delete(id);
        log.debug("The user " + id + " delete.");
    }
}
