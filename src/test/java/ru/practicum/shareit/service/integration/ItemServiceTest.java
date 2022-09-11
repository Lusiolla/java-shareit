package ru.practicum.shareit.service.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
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
public class ItemServiceTest {

    private final ItemService service;
    private final ItemMapper mapper;
    private final EntityManager em;

    @Test
    public void shouldGetItemsWithoutPagination() {

        User user1 = new User(
                null,
                "test22@ya.ru",
                "user1"
        );
        User user2 = new User(null,
                "test23@ya.ru",
                "user2"
        );

        em.persist(user1);
        em.persist(user2);
        em.flush();

        Item item1 = new Item(
                null,
                "test1",
                "description1",
                true,
                user1.getId(),
                null,
                null
        );
        Item item2 = new Item(
                null,
                "test2",
                "description2",
                false,
                user1.getId(),
                null,
                null
        );
        Item item3 = new Item(
                null,
                "test3",
                "description3",
                true,
                user2.getId(),
                null,
                null
        );

        em.persist(item1);
        em.persist(item2);
        em.persist(item3);
        em.flush();

        List<Item> entityItems = List.of(
                item1,
                item2
        );

        Collection<ItemDto> sourceItems = entityItems.stream().map(mapper::mapToItemDto).collect(Collectors.toList());

        Collection<ItemDto> targetItems = service.getItems(user1.getId(), null, null);

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

        User user1 = new User(
                null,
                "test24@ya.ru",
                "user1"
        );
        User user2 = new User(null,
                "test25@ya.ru",
                "user2"
        );

        em.persist(user1);
        em.persist(user2);
        em.flush();

        Item item1 = new Item(
                null,
                "test1",
                "description1",
                true,
                user1.getId(),
                null,
                null
        );
        Item item2 = new Item(
                null,
                "test2",
                "description2",
                false,
                user1.getId(),
                null,
                null
        );
        Item item3 = new Item(
                null,
                "test3",
                "description3",
                true,
                user2.getId(),
                null,
                null
        );

        em.persist(item1);
        em.persist(item2);
        em.persist(item3);
        em.flush();

        List<Item> entityItems = List.of(
                item1
        );

        Collection<ItemDto> sourceItems = entityItems.stream().map(mapper::mapToItemDto).collect(Collectors.toList());

        Collection<ItemDto> targetItems = service.getItems(user1.getId(), 0, 1);

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
