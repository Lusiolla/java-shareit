package ru.practicum.shareit.service.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.requests.ItemRequestMapper;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.practicum.shareit.TestUtil.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {

    private final ItemRequestService service;
    private final UserService userService;
    private final ItemRequestMapper mapper;

    @BeforeTransaction
    public void setUp() {
        userService.add(user1);
        userService.add(user2);
        service.add(itemRequest1);
        service.add(itemRequest2);
    }

    @Test
    public void shouldGetItemsWithoutPagination() {
        List<ItemRequest> entityItemRequests = List.of(
                itemRequest2
        );

        Collection<ItemRequestDto> sourceItemRequests = entityItemRequests.stream().map(mapper::mapToRequestDto).collect(Collectors.toList());

        Collection<ItemRequestDto> targetItemRequests = service.getAll(1L, null, null);

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
        List<ItemRequest> entityItemRequests = List.of(
                itemRequest1
        );

        Collection<ItemRequestDto> sourceItemRequests = entityItemRequests.stream().map(mapper::mapToRequestDto).collect(Collectors.toList());

        Collection<ItemRequestDto> targetItemRequests = service.getAll(2L, 0, 1);

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
