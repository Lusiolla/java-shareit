package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.dto.UserUpdate;
import ru.practicum.shareit.user.model.User;

import java.util.Map;
import java.util.Optional;

@Component
public class UserMapper {
    //из user в dto
    public UserDTO mapToUserResponse(User user) {
        UserDTO response = new UserDTO();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        return response;
    }

    //из dto в user
    public User mapToUser(UserDTO response) {
        User user = new User();
        user.setId(response.getId());
        user.setName(response.getName());
        user.setEmail(response.getEmail());
        return user;
    }

    //из map в update
    public UserUpdate mapToUserUpdate(Map<String, Object> updates) {
        UserUpdate update = new UserUpdate();
        update.setName(updates.get("name") != null ? Optional.of(updates.get("name").toString()) : Optional.empty());
        update.setEmail(updates.get("email") != null ? Optional.of(updates.get("email").toString()) : Optional.empty());
        return update;
    }
}
