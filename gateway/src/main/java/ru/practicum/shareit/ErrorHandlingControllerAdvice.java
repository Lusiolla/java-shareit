package ru.practicum.shareit;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Component
public class ErrorHandlingControllerAdvice {
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    protected ResponseEntity<Exception> handleThereIsNoValidationException() {
        Exception e = new Exception("Unknown state: UNSUPPORTED_STATUS");
        return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }

    @Data
    @AllArgsConstructor
    private static class Exception {
        private String error;
    }
}
