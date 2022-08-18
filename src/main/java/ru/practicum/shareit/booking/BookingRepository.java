package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select * " +
            "from bookings as b " +
            "where item_id = ? " +
            "and (start_booking, end_booking) overlaps (?, ?)",
            nativeQuery = true)
    Collection<Booking> findIntersectionsByItemId(long itemId, LocalDateTime start, LocalDateTime end);

    Collection<Booking> findAllByUserIdOrderByStartBookingDesc(long userId);
    Collection<Booking> findAllByUserIdAndStartBookingLessThanEqualAndEndBookingAfterOrderByStartBookingDesc(
            long userId, LocalDateTime start, LocalDateTime end);
    Collection<Booking> findAllByUserIdAndEndBookingBeforeOrderByStartBookingDesc(long userId, LocalDateTime now);

    Collection<Booking> findAllByUserIdAndStartBookingAfterOrderByStartBookingDesc(long userId, LocalDateTime now);

    Collection<Booking> findAllByUserIdAndStatusOrderByStartBookingDesc(long userId, Status status);

    Collection<Booking> findAllByItemIdInOrderByStartBookingDesc(Collection<Long> itemId);

    Collection<Booking> findAllByItemIdInAndStartBookingBeforeAndEndBookingGreaterThanEqualOrderByStartBookingDesc(
            Collection<Long> itemId, LocalDateTime start, LocalDateTime end);

    Collection<Booking> findAllByItemIdInAndEndBookingBeforeOrderByStartBookingDesc(
            Collection<Long> itemId, LocalDateTime now);

    Collection<Booking> findAllByItemIdInAndStartBookingAfterOrderByStartBookingDesc(
            Collection<Long> itemId, LocalDateTime now);

    Collection<Booking> findAllByItemIdInAndStatusOrderByStartBookingDesc(Collection<Long> itemId, Status waiting);

    Collection<Booking> findByUserIdAndItemIdAndStartBookingBeforeAndStatus(Long id, long itemId, LocalDateTime now,
                                                                            Status status);

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







