package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.Status;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class BookingCreate {
    private Long id;
    @FutureOrPresent
    private LocalDateTime start;
    @FutureOrPresent
    private LocalDateTime end;
    @NotNull
    private Long itemId;
    private Long bookerId;
    private Status status = Status.WAITING;
}
