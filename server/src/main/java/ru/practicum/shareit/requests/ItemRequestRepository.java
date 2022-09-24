package ru.practicum.shareit.requests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    Collection<ItemRequest> findAllByUserIdOrderByCreatedDesc(long userId);

    @Query(value = "select * " +
            "from requests as r " +
            "where r.user_id <> ? " +
            "order by r.created desc " +
            "limit ? " +
            "offset ?",
            nativeQuery = true)
    Collection<ItemRequest> findAllByParam(long userId, int size, int from);

    Collection<ItemRequest> findAllByUserIdNotOrderByCreatedDesc(long userId);
}
