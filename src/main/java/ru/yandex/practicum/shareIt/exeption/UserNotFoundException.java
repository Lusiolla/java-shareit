package ru.yandex.practicum.filmorate.exeption;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
    }

    public UserNotFoundException(final String message) {
        super(message);
    }
}
