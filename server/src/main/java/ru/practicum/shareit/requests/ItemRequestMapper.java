package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ItemRequestMapper {
    private final ItemMapper itemMapper;

    // из request в dto
    public ItemRequestDto mapToRequestDto(ItemRequest request) {
        ItemRequestDto response = new ItemRequestDto();
        response.setId(request.getId());
        response.setDescription(request.getDescription());
        response.setCreated(request.getCreated());
        if (request.getResponses() != null) {
            response.setItems(request.getResponses()
                    .stream().map(itemMapper::mapToItemShort)
                    .collect(Collectors.toList()));
        }
        return response;
    }

    // из dto в request
    public ItemRequest mapToRequest(ItemRequestDto dto) {
        ItemRequest request = new ItemRequest();
        request.setId(dto.getId());
        request.setDescription(dto.getDescription());
        request.setCreated(dto.getCreated());
        return request;
    }

}
