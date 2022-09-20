package ru.practicum.shareit.service.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdate;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {

    private final UserService service;
    private final EntityManager em;
    private final UserMapper mapper;

    @Test
    public void shouldUpdateUser() {
        User user1 = new User(
                null,
                "test26@ya.ru",
                "user1");

        em.persist(user1);
        em.flush();

        User userUpd = new User(
                user1.getId(),
                "test26@ya.ru",
                "user1upd"
        );

        UserDto userDto = mapper.mapToUserDto(userUpd);

        service.update(new UserUpdate(
                user1.getId(),
                Optional.empty(),
                Optional.of("user1upd")
        ));

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", userDto.getEmail())
                .getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));

    }
}
