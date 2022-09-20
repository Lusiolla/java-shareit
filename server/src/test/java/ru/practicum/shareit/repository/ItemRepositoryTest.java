package ru.practicum.shareit.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRepositoryTest {

    private final TestEntityManager em;
    private final ItemRepository itemRepository;

    @Test
    public void search() {
        User newUser2 = new User(
                null,
                "test2@ya.ru",
                "user2"
        );
        em.persist(newUser2);
        em.flush();

        List<Item> sourceItems = List.of(
                new Item(
                        null,
                        "test2",
                        "description2",
                        true,
                        newUser2.getId(),
                        null,
                        null
                ),

                new Item(
                        null,
                        "test2",
                        "description2",
                        true,
                        newUser2.getId(),
                        null,
                        null
                )

        );
        sourceItems.forEach(em::persist);
        em.flush();

        Collection<Item> targetItems = itemRepository.search(
                "test2", 1, 0);
        Collection<Item> emptyItems = itemRepository.search(
                "test2", 0, 0);

        assertThat(targetItems, hasSize(1));
        sourceItems.forEach(sourceItem -> assertThat(targetItems, hasItem(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("name", equalTo(sourceItem.getName())),
                hasProperty("description", equalTo(sourceItem.getDescription())),
                hasProperty("available", equalTo(sourceItem.getAvailable()))
        ))));
        assertThat(emptyItems, hasSize(0));
    }

    @Test
    public void findAllByParam() {
        User newUser3 = new User(
                null,
                "test3@ya.ru",
                "user3"
        );
        em.persist(newUser3);
        em.flush();

        List<Item> sourceItems = List.of(
                new Item(
                        null,
                        "test1",
                        "description1",
                        true,
                        newUser3.getId(),
                        null,
                        null
                ),
                new Item(
                        null,
                        "test2",
                        "description2",
                        true,
                        newUser3.getId(),
                        null,
                        null
                )
        );
        sourceItems.forEach(em::persist);
        em.flush();

        Collection<Item> targetItems = itemRepository.findAllByParam(newUser3.getId(), 2, 0);

        assertThat(targetItems, hasSize(sourceItems.size()));
        sourceItems.forEach(sourceItem -> assertThat(targetItems, hasItem(allOf(
                hasProperty("id", notNullValue()),
                hasProperty("name", equalTo(sourceItem.getName())),
                hasProperty("description", equalTo(sourceItem.getDescription())),
                hasProperty("available", equalTo(sourceItem.getAvailable()))
        ))));
    }
}
