package ru.yandex.practicum.shareIt.user;

import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class UserMapper {
    //из user в userResponse
    public UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        return response;
    }

    //из userResponse в user
    public User mapToUser(UserResponse response) {
        User user = new User();
        user.setId(response.getId());
        user.setName(response.getName());
        user.setEmail(response.getEmail());
        return user;
    }

    //из user в userUpdate
    public UserUpdate mapToUserUpdate(User user) {
        UserUpdate update = new UserUpdate();
        update.setId(user.getId());
        update.setName(Optional.ofNullable(user.getName()));
        update.setEmail(Optional.ofNullable(user.getEmail()));
        return update;
    }
}
