package ru.yandex.practicum.shareIt.exeption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class ErrorHandlingControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseBody
    protected ResponseEntity<Exception> handleUserAlreadyExistException() {
        Exception e = new Exception("The user is already exist");
        log.warn(e.getMessage());
        return new ResponseEntity<>(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseBody
    protected ResponseEntity<Exception> handleItemNotFoundException() {
        Exception e = new Exception("The item not found");
        log.warn(e.getMessage());
        return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    protected ResponseEntity<Exception> handleUserNotFoundException() {
        Exception e = new Exception("The user not found");
        log.warn(e.getMessage());
        return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
    }

    @Data
    @AllArgsConstructor
    private static class Exception {
        private String message;
    }
}
