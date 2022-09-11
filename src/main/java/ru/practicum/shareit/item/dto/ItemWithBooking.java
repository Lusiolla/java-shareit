package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.dto.BookingShort;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemWithBooking extends ItemDto {
    private BookingShort lastBooking;
    private BookingShort nextBooking;

    public ItemWithBooking(
            Long id,
            @NotNull @NotBlank String name,
            @NotNull @NotBlank String description,
            @NotNull Boolean available,
            Long requestId,
            Collection<CommentDto> comments,
            BookingShort lastBooking,
            BookingShort nextBooking) {
        super(id, name, description, available, requestId, comments);
        this.lastBooking = lastBooking;
        this.nextBooking = nextBooking;
    }

}
