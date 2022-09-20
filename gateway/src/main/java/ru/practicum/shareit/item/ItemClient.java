package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentRequest;
import ru.practicum.shareit.item.dto.ItemRequest;
import ru.practicum.shareit.item.dto.ItemUpdate;

import java.util.Map;
import java.util.Optional;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getItems(long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getItem(Long userId, Long id) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> searchForItems(String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", null, parameters);
    }

    public ResponseEntity<Object> addNewItem(Long userId, ItemRequest itemRequest) {

        return post("", userId, itemRequest);
    }

    public ResponseEntity<Object> addNewComment(Long userId, CommentRequest commentRequest, Long itemId) {

        return post("/" + itemId + "/comment", userId, commentRequest);
    }

    public ResponseEntity<Object> updateItem(Long userId, Map<String, Object> updates, Long itemId) {
        ItemUpdate update = new ItemUpdate();
        update.setName(updates.get("name") != null ? Optional.of(updates.get("name").toString()) : Optional.empty());
        update.setDescription(updates.get("description") != null ? Optional.of(updates.get("description").toString())
                : Optional.empty());
        update.setAvailable(updates.get("available") != null ? Optional.of((Boolean) updates.get("available"))
                : Optional.empty());
        return patch("/" + itemId, userId, update);
    }

    public ResponseEntity<Object> deleteItem(Long userId, Long itemId) {

        return delete("/" + itemId, userId);
    }


}
