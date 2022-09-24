package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exeption.*;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.TestUtil.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    private final ObjectMapper mapper;
    @MockBean
    private final BookingService bookingService;
    @MockBean
    private final BookingMapper bookingMapper;
    private final MockMvc mvc;

    @Test
    void add() throws Exception {
        when(bookingService.add(any(), anyLong()))
                .thenReturn(bookingCreate1);
        when(bookingMapper.mapToBooking(any()))
                .thenReturn(booking1);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingCreate1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingCreate1.getId()), Long.class))
                .andExpect(jsonPath("$.bookerId", is(bookingCreate1.getBookerId()), Long.class))
                .andExpect(jsonPath("$.itemId", is(bookingCreate1.getItemId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingCreate1.getStatus().toString())))
                .andExpect(jsonPath("$.start", is(bookingCreate1.getStart()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.end", is(bookingCreate1.getEnd()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @Test
    void shouldThrowUserNodFoundExceptionInAdd() throws Exception {

        when(bookingService.add(any(), anyLong()))
                .thenThrow(UserNotFoundException.class);

        when(bookingMapper.mapToBooking(any()))
                .thenReturn(booking1);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingCreate1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(404));
    }

    @Test
    void shouldThrowItemNodFoundExceptionInAdd() throws Exception {

        when(bookingService.add(any(), anyLong()))
                .thenThrow(ItemNotFoundException.class);

        when(bookingMapper.mapToBooking(any()))
                .thenReturn(booking1);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingCreate1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(404));
    }

    @Test
    void shouldItemNotAvailableExceptionInAdd() throws Exception {

        when(bookingService.add(any(), anyLong()))
                .thenThrow(ItemNotAvailableException.class);

        when(bookingMapper.mapToBooking(any()))
                .thenReturn(booking1);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingCreate1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldBookingForbiddenExceptionInAdd() throws Exception {

        when(bookingService.add(any(), anyLong()))
                .thenThrow(BookingForbiddenException.class);

        when(bookingMapper.mapToBooking(any()))
                .thenReturn(booking1);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingCreate1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(404));
    }

    @Test
    void approved() throws Exception {
        when(bookingService.approved(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto1);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto1.getStatus().toString())))
                .andExpect(jsonPath("$.start", is(bookingDto1.getStart()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.end", is(bookingDto1.getEnd()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @Test
    void shouldIllegalStateExceptionInApproved() throws Exception {

        when(bookingService.approved(anyLong(), anyLong(), anyBoolean()))
                .thenThrow(IllegalStateException.class);

        when(bookingMapper.mapToBooking(any()))
                .thenReturn(booking1);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(400));
    }

    @Test
    void getById() throws Exception {
        when(bookingService.getById(anyLong(), anyLong()))
                .thenReturn(bookingDto1);

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto1.getStatus().toString())))
                .andExpect(jsonPath("$.start", is(bookingDto1.getStart()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.end", is(bookingDto1.getEnd()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @Test
    void shouldBookingNotFoundExceptionInGetById() throws Exception {

        when(bookingService.getById(anyLong(), anyLong()))
                .thenThrow(BookingNotFoundException.class);

        when(bookingMapper.mapToBooking(any()))
                .thenReturn(booking1);

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(404));
    }

    @Test
    void getAllBookingByUserId() throws Exception {
        when(bookingService.getAllBookingByUserId(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto1));

        mvc.perform(get("/bookings", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto1.getStatus().toString())))
                .andExpect(jsonPath("$[0].start", is(bookingDto1.getStart()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end", is(bookingDto1.getEnd()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @Test
    void shouldReturnUNSUPPORTED_STATUSInGetAllBooking() throws Exception {

        when(bookingService.add(any(), anyLong()))
                .thenThrow(MethodArgumentTypeMismatchException.class);

        when(bookingMapper.mapToBooking(any()))
                .thenReturn(booking1);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "UNSUPPORTED_STATUS")
                        .content(mapper.writeValueAsString(bookingCreate1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllBookingByUserIdWithPagination() throws Exception {
        when(bookingService.getAllBookingByUserId(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto1));

        mvc.perform(get("/bookings", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto1.getStatus().toString())))
                .andExpect(jsonPath("$[0].start", is(bookingDto1.getStart()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end", is(bookingDto1.getEnd()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }

    @Test
    void getAllBookingItemsByUserId() throws Exception {
        when(bookingService.getAllBookedItemsByUserId(anyLong(), any(State.class), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto1));

        mvc.perform(get("/bookings/owner", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingDto1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingDto1.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto1.getStatus().toString())))
                .andExpect(jsonPath("$[0].start", is(bookingDto1.getStart()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$[0].end", is(bookingDto1.getEnd()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))));
    }
}
