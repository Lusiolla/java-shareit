package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            "or upper(i.description) like upper(concat('%', ?1, '%')) " +
            "and i.available = true ")
    Collection<Item> search(String text);

    @Query(value = "select * from items as i " +
            "where upper(i.name) = upper(?1) " +
            "or upper(i.description) = upper(?1) " +
            "and i.is_available = true " +
            "limit ?2 " +
            "offset ?3",
            nativeQuery = true)
    Collection<Item> searchByParam(String text, int size, int from);

    @Query(value = "select * " +
            "from items as i " +
            "where i.user_id = ? " +
            "limit ? " +
            "offset ?",
            nativeQuery = true)
    Collection<Item> findAllByParam(long userId, int size, int from);

    void deleteByIdAndUserId(long userId, long itemId);

    Collection<Item> findAllByUserId(long userId);
}
