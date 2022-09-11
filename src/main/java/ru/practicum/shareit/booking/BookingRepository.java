package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking> {

    @Query(value = "select * " +
            "from bookings as b " +
            "where b.item_id = ?1 " +
            "and b.end_booking < ?2 " +
            "order by b.end_booking desc " +
            "limit 1", nativeQuery = true)
    Optional<Booking> findLastBooking(long id, LocalDateTime now);

    @Query(value = "select * " +
            "from bookings as b " +
            "where b.item_id = ? " +
            "and b.start_booking > ? " +
            "order by b.start_booking " +
            "limit 1", nativeQuery = true)
    Optional<Booking> findNextBooking(long id, LocalDateTime now);

}







