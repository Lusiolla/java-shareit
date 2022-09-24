package ru.practicum.shareit.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static ru.practicum.shareit.TestUtil.itemRequestDto1;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestJsonTest {

    private final JacksonTester<ItemRequestDto> json;

    @Test
    void testRequestDto() throws Exception {

        JsonContent<ItemRequestDto> result = json.write(itemRequestDto1);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.items").isEqualTo(null);

    }
}
