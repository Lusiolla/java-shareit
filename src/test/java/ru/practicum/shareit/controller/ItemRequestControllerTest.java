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
import ru.practicum.shareit.exeption.ItemRequestNotFoundException;
import ru.practicum.shareit.requests.ItemRequestController;
import ru.practicum.shareit.requests.ItemRequestMapper;
import ru.practicum.shareit.requests.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.TestUtil.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    private final ObjectMapper mapper;
    @MockBean
    private ItemRequestService service;
    @MockBean
    private ItemRequestMapper itemRequestMapper;
    private final MockMvc mvc;

    @Test
    void add() throws Exception {
        when(service.add(any()))
                .thenReturn(itemRequestDto1);
        when(itemRequestMapper.mapToRequest(any()))
                .thenReturn(itemRequest1);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto1.getDescription())))
                .andExpect(jsonPath("$.items", is(itemRequestDto1.getItems())))
                .andExpect(jsonPath("$.created", is(itemRequestDto1.getCreated())));
    }

    @Test
    void getAllRequestsToUser() throws Exception {
        when(service.getAllByUserId(anyLong()))
                .thenReturn(List.of(itemRequestDto1));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequestDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto1.getDescription())))
                .andExpect(jsonPath("$[0].items", is(itemRequestDto1.getItems())))
                .andExpect(jsonPath("$[0].created", is(itemRequestDto1.getCreated())));
    }

    @Test
    void getAll() throws Exception {
        when(service.getAll(anyLong(), any(), any()))
                .thenReturn(List.of(itemRequestDto1));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemRequestDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto1.getDescription())))
                .andExpect(jsonPath("$[0].items", is(itemRequestDto1.getItems())))
                .andExpect(jsonPath("$[0].created", is(itemRequestDto1.getCreated())));
    }

    @Test
    void getById() throws Exception {
        when(service.getById(anyLong(), anyLong()))
                .thenReturn(itemRequestDto1);

        mvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto1.getDescription())))
                .andExpect(jsonPath("$.items", is(itemRequestDto1.getItems())))
                .andExpect(jsonPath("$.created", is(itemRequestDto1.getCreated())));
    }

    @Test
    void shouldThrowItemRequestNotFoundExceptionInGetById() throws Exception {

        when(service.getById(anyLong(), anyLong()))
                .thenThrow(ItemRequestNotFoundException.class);

        mvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(404));
    }
}
