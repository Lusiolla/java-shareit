package ru.yandex.practicum.shareIt.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserResponse> getAll() {
        Collection<UserResponse> response = userService.getAll();
        log.debug("Returned the list of users: {}", response.size());
        return response;
    }

    @GetMapping("{id}")
    public UserResponse getById(@Valid @NotNull @PathVariable Long id) {
        UserResponse user = userService.getById(id);
        log.debug("Returned the user: {}", user.getId());
        return user;
    }

    @PostMapping
    public UserResponse add(@Valid @NotNull @RequestBody User newUser) {
        UserResponse user = userService.add(newUser);
        log.debug("User added: {}", user.getId());
        return user;
    }

    @PatchMapping("{id}")
    public UserResponse update(@RequestBody User updateUser,
                               @Valid @NotNull @PathVariable Long id) {
        updateUser.setId(id);
        UserResponse user = userService.update(updateUser);
        log.debug("Updated user information: {}", user.getId());
        return user;
    }

    @DeleteMapping("{id}")
    public void delete(@Valid @NotNull @PathVariable Long id) {
        userService.delete(id);
        log.debug("The user " + id + " delete.");
    }
}