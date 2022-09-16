package ru.practicum.shareit.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static ru.practicum.shareit.TestUtil.commentDto1;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentDtoJsonTest {
    private final JacksonTester<CommentDto> json;

    @Test
    void testCommentDto() throws Exception {
        JsonContent<CommentDto> result = json.write(commentDto1);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("good");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("name1");
        assertThat(result).extractingJsonPathBooleanValue("$.created").isEqualTo(null);
    }
}
