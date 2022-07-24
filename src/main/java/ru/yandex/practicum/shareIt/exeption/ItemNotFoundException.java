package ru.yandex.practicum.shareIt.exeption;

public class FilmNotFoundException extends RuntimeException{
    public FilmNotFoundException() {
    }

    public FilmNotFoundException(final String message) {
        super(message);
    }
}
