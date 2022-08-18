package ru.practicum.shareit.requests;

import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.model.ItemRequest;

public class ItemRequestMapper {
    //из request в dto
    public ItemRequestDto mapToRequestResponse(ItemRequest request) {
        ItemRequestDto response = new ItemRequestDto();
        response.setId(request.getId());
        response.setName(request.getName());
        response.setDescription(request.getDescription());
        return response;
    }

    //из dto в request
    public ItemRequest mapToRequest(ItemRequestDto dto) {
        ItemRequest request = new ItemRequest();
        request.setId(dto.getId());
        request.setName(dto.getName());
        request.setDescription(dto.getDescription());
        return request;
    }

}
