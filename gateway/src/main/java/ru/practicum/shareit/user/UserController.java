package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> add(@Valid @NotNull @RequestBody UserDto newUser) {
        log.debug("User added: {}", newUser.getName());
        return userClient.addNewUser(newUser);
    }

    @PatchMapping("{id}")
    public ResponseEntity<Object> update(@NotNull @RequestBody Map<String, Object> updates,
                                         @NotNull @PathVariable Long id) {
        log.debug("Updated user information: {}", id);
        return userClient.updateUser(updates, id);
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getById(@NotNull @PathVariable Long id) {
        log.debug("Returned the user: {}", id);
        return userClient.getUser(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.debug("Returned the list of users");
        return userClient.getUsers();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@NotNull @PathVariable Long id) {
        log.debug("The user " + id + " delete.");
        return userClient.deleteUser(id);
    }

}
