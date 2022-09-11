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
import ru.practicum.shareit.exeption.CommentAlreadyExistException;
import ru.practicum.shareit.exeption.CommentForbiddenException;
import ru.practicum.shareit.exeption.ItemNotFoundException;
import ru.practicum.shareit.exeption.UserNotFoundException;
import ru.practicum.shareit.item.CommentMapper;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.TestUtil.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    private final ObjectMapper mapper;
    @MockBean
    private ItemService itemService;
    @MockBean
    private ItemMapper itemMapper;
    @MockBean
    private CommentMapper commentMapper;
    private final MockMvc mvc;

    @Test
    void add() throws Exception {

        when(itemService.add(any(), anyLong()))
                .thenReturn(itemDto1);

        when(itemMapper.mapToItem(any()))
                .thenReturn(item1);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto1.getName())))
                .andExpect(jsonPath("$.description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto1.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto1.getRequestId())))
                .andExpect(jsonPath("$.comments", is(itemDto1.getComments())));
    }

    @Test
    void addComment() throws Exception {

        when(itemService.addComment(any()))
                .thenReturn(commentDto1);

        when(commentMapper.mapToComment(any(), anyLong(), anyLong()))
                .thenReturn(comment1);

        mvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto1.getId()), Long.class))
                .andExpect(jsonPath("$.authorName", is(commentDto1.getAuthorName())))
                .andExpect(jsonPath("$.text", is(commentDto1.getText())))
                .andExpect(jsonPath("$.created", is(commentDto1.getCreated())));
    }

    @Test
    void shouldThrowUserNodFoundExceptionInAddComment() throws Exception {

        when(itemService.addComment(any()))
                .thenThrow(UserNotFoundException.class);

        when(commentMapper.mapToComment(any(), anyLong(), anyLong()))
                .thenReturn(comment1);

        mvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(404));
    }

    @Test
    void shouldThrowItemNodFoundExceptionInAddComment() throws Exception {

        when(itemService.addComment(any()))
                .thenThrow(ItemNotFoundException.class);

        when(commentMapper.mapToComment(any(), anyLong(), anyLong()))
                .thenReturn(comment1);

        mvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(404));
    }

    @Test
    void shouldThrowCommentForbiddenExceptionInAddComment() throws Exception {

        when(itemService.addComment(any()))
                .thenThrow(CommentForbiddenException.class);

        when(commentMapper.mapToComment(any(), anyLong(), anyLong()))
                .thenReturn(comment1);

        mvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(400));
    }

    @Test
    void shouldThrowCommentAlreadyExistExceptionInAddComment() throws Exception {

        when(itemService.addComment(any()))
                .thenThrow(CommentAlreadyExistException.class);

        when(commentMapper.mapToComment(any(), anyLong(), anyLong()))
                .thenReturn(comment1);

        mvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(409));
    }

    @Test
    void getAllItemsToUserWithoutPagination() throws Exception {

        when(itemService.getItems(anyLong(), any(), any()))
                .thenReturn(List.of(itemDto1));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto1.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto1.getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(itemDto1.getRequestId())))
                .andExpect(jsonPath("$[0].comments", is(itemDto1.getComments())));
    }

    @Test
    void getAllItemsToUserWithPagination() throws Exception {
        when(itemService.getItems(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto1));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto1.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto1.getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(itemDto1.getRequestId())))
                .andExpect(jsonPath("$[0].comments", is(itemDto1.getComments())));
    }


    @Test
    void searchWithoutPagination() throws Exception {
        when(itemService.search(anyString(), any(), any()))
                .thenReturn(List.of(itemDto1));

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "item1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto1.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto1.getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(itemDto1.getRequestId())))
                .andExpect(jsonPath("$[0].comments", is(itemDto1.getComments())));
    }

    @Test
    void searchWithPagination() throws Exception {

        when(itemService.search(anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(itemDto1));

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "item1")
                        .param("from", "0")
                        .param("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto1.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto1.getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(itemDto1.getRequestId())))
                .andExpect(jsonPath("$[0].comments", is(itemDto1.getComments())));
    }

    @Test
    void getById() throws Exception {

        when(itemService.getById(anyLong(), anyLong()))
                .thenReturn(itemDto1);

        mvc.perform(get("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto1.getName())))
                .andExpect(jsonPath("$.description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto1.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto1.getRequestId())))
                .andExpect(jsonPath("$.comments", is(itemDto1.getComments())));

    }

    @Test
    void update() throws Exception {

        when(itemService.update(any(), anyLong(), anyLong()))
                .thenReturn(itemDtoAfterUpd);

        when(itemMapper.mapToItemUpdate(anyMap()))
                .thenReturn(itemUpdate1);

        mvc.perform(patch("/items/{id}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(anyMap()))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoAfterUpd.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoAfterUpd.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoAfterUpd.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoAfterUpd.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDtoAfterUpd.getRequestId())))
                .andExpect(jsonPath("$.comments", is(itemDtoAfterUpd.getComments())));
    }

    @Test
    void deleteById() throws Exception {

        mvc.perform(delete("/items/{itemId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
