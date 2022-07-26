package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UserDTO {
    private Long id;
    @NotBlank
    @Email
    @Pattern(regexp = "^\\S*$")
    private String email;
    @NotNull
    @NotBlank
    private String name;
}
