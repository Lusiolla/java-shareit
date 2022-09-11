package ru.practicum.shareit.service.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
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
public class ItemServiceTest {

    private final ItemService service;
    private final UserService userService;
    private final ItemMapper mapper;

    @BeforeTransaction
    public void setUp() {
        userService.add(user1);
        userService.add(user2);
        service.add(item1, 1L);
        service.add(item2, 1L);
        service.add(item3, 2L);
    }

    @Test
    public void shouldGetItemsWithoutPagination() {
        List<Item> entityItems = List.of(
                item1,
                item2
        );

        Collection<ItemDto> sourceItems = entityItems.stream().map(mapper::mapToItemDto).collect(Collectors.toList());

        Collection<ItemDto> targetItems = service.getItems(1L, null, null);

        assertThat(targetItems, hasSize(sourceItems.size()));
        for (ItemDto sourceItem : sourceItems) {
            assertThat(targetItems, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(sourceItem.getName())),
                    hasProperty("description", equalTo(sourceItem.getDescription())),
                    hasProperty("available", equalTo(sourceItem.getAvailable()))
            )));
        }
    }

    @Test
    public void shouldGetItemsWithPagination() {
        List<Item> entityItems = List.of(
                item1
        );

        Collection<ItemDto> sourceItems = entityItems.stream().map(mapper::mapToItemDto).collect(Collectors.toList());

        Collection<ItemDto> targetItems = service.getItems(1L, 0, 1);

        assertThat(targetItems, hasSize(sourceItems.size()));
        for (ItemDto sourceItem : sourceItems) {
            assertThat(targetItems, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(sourceItem.getName())),
                    hasProperty("description", equalTo(sourceItem.getDescription())),
                    hasProperty("available", equalTo(sourceItem.getAvailable()))
            )));
        }
    }
}
