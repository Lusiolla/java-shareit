package ru.practicum.shareit.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingRepositoryTest {

    private final TestEntityManager em;
    private final BookingRepository repository;

    @Test
    public void findLastBooking() {
        User newUser2 = new User(
                null,
                "t4@ya.ru",
                "user4"
        );

        em.persist(newUser2);
        em.flush();

        Item newItem2 = new Item(
                null,
                "test1",
                "description1",
                true,
                newUser2.getId(),
                null,
                null
        );
        em.persist(newItem2);
        em.flush();

        Booking newBooking2 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 5, 15, 56),
                LocalDateTime.of(2022, 9, 6, 15, 56),
                newItem2,
                newUser2,
                Status.APPROVED
        );

        em.persist(newBooking2);
        em.flush();

        Booking targetBooking = repository.findLastBooking(newItem2.getId(), LocalDateTime.now()).orElse(null);
        Booking emptyBooking = repository.findLastBooking(1L, LocalDateTime.now()).orElse(null);

        assertEquals(targetBooking, newBooking2, "Вернулось не последние бронирование");
        assertNull(emptyBooking, "Вернулся не пустой запрос");
    }

    @Test
    public void findNextBooking() {
        User newUser1 = new User(
                null,
                "t5@ya.ru",
                "user5"
        );
        em.persist(newUser1);

        Item newItem1 = new Item(
                null,
                "test1",
                "description1",
                true,
                newUser1.getId(),
                null,
                null
        );
        em.persist(newItem1);

        Booking newBooking1 = new Booking(
                null,
                LocalDateTime.of(2022, 9, 17, 15, 56),
                LocalDateTime.of(2022, 9, 18, 15, 56),
                newItem1,
                newUser1,
                Status.APPROVED
        );

        em.persist(newBooking1);
        em.flush();

        Booking targetBooking = repository.findNextBooking(newItem1.getId(), LocalDateTime.now()).orElse(null);
        Booking emptyBooking = repository.findNextBooking(3L, LocalDateTime.now()).orElse(null);

        assertEquals(targetBooking, newBooking1, "Вернулось не следующее бронирование");
        assertNull(emptyBooking, "Вернулся не пустой запрос");

    }
}
