package ru.practicum.shareit.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static ru.practicum.shareit.TestUtil.userDto1;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDtoJsonTest {

    private final JacksonTester<UserDto> json;

    @Test
    void testUserDto() throws Exception {

        JsonContent<UserDto> result = json.write(userDto1);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("user1");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("test1@ya.ru");
    }
}
