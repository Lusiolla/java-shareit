package ru.practicum.shareit.service.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.requests.ItemRequestMapper;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {

    private final ItemRequestService service;
    private final ItemRequestMapper mapper;
    private final EntityManager em;

    @Test
    public void shouldGetItemsWithoutPagination() {

        User user1 = new User(
                null,
                "test18@ya.ru",
                "user1"
        );
        User user2 = new User(null,
                "test19@ya.ru",
                "user2"
        );

        em.persist(user1);
        em.persist(user2);
        em.flush();

        ItemRequest itemRequest1 = new ItemRequest(
                null,
                "description1",
                user1.getId(),
                LocalDateTime.now(),
                null
        );
        ItemRequest itemRequest2 = new ItemRequest(
                null,
                "description2",
                user2.getId(),
                LocalDateTime.now(),
                null
        );

        em.persist(itemRequest1);
        em.persist(itemRequest2);
        em.flush();

        List<ItemRequest> entityItemRequests = List.of(
                itemRequest2
        );

        Collection<ItemRequestDto> sourceItemRequests = entityItemRequests.stream()
                .map(mapper::mapToRequestDto).collect(Collectors.toList());

        Collection<ItemRequestDto> targetItemRequests = service.getAll(user1.getId(), null, null);

        assertThat(targetItemRequests, hasSize(sourceItemRequests.size()));
        for (ItemRequestDto sourceItemRequest : sourceItemRequests) {
            assertThat(targetItemRequests, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("description", equalTo(sourceItemRequest.getDescription())),
                    hasProperty("created", equalTo(sourceItemRequest.getCreated()))
            )));
        }
    }

    @Test
    public void shouldGetItemsWithPagination() {

        User user1 = new User(
                null,
                "test20@ya.ru",
                "user1"
        );
        User user2 = new User(null,
                "test21@ya.ru",
                "user2"
        );

        em.persist(user1);
        em.persist(user2);
        em.flush();

        ItemRequest itemRequest1 = new ItemRequest(
                null,
                "description1",
                user1.getId(),
                LocalDateTime.now(),
                null
        );

        ItemRequest itemRequest2 = new ItemRequest(
                null,
                "description2",
                user2.getId(),
                LocalDateTime.now(),
                null
        );

        em.persist(itemRequest1);
        em.persist(itemRequest2);
        em.flush();

        List<ItemRequest> entityItemRequests = List.of(
                itemRequest1
        );

        Collection<ItemRequestDto> sourceItemRequests = entityItemRequests.stream()
                .map(mapper::mapToRequestDto).collect(Collectors.toList());

        Collection<ItemRequestDto> targetItemRequests = service.getAll(user2.getId(), 0, 1);

        assertThat(targetItemRequests, hasSize(sourceItemRequests.size()));
        for (ItemRequestDto sourceItemRequest : sourceItemRequests) {
            assertThat(targetItemRequests, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("description", equalTo(sourceItemRequest.getDescription())),
                    hasProperty("created", equalTo(sourceItemRequest.getCreated()))
            )));
        }
    }

}
