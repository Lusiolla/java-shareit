package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingShort;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemWithBooking extends ItemDto {
    private BookingShort lastBooking;
    private BookingShort nextBooking;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ItemWithBooking that = (ItemWithBooking) o;
        return Objects.equals(lastBooking, that.lastBooking) && Objects.equals(nextBooking, that.nextBooking);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lastBooking, nextBooking);
    }
}
