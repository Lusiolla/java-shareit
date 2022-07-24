package ru.yandex.practicum.shareIt.exeption;

public class FilmAlreadyExistException extends RuntimeException {

    public FilmAlreadyExistException() {
    }

    public FilmAlreadyExistException(final String message) {
        super(message);
    }
}
