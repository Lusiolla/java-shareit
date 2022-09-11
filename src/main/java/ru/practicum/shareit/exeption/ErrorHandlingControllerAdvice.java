package ru.practicum.shareit.exeption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
@Component
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    protected ResponseEntity<Exception> handleThereIsNoValidationException() {
        Exception e = new Exception("Unknown state: UNSUPPORTED_STATUS");
        log.warn(e.getError());
        return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseBody
    protected ResponseEntity<Exception> handleIllegalArgumentException() {
        Exception e = new Exception("Illegal state");
        log.warn(e.getError());
        return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookingNotFoundException.class)
    @ResponseBody
    protected ResponseEntity<Exception> handleBookingNotFoundException() {
        Exception e = new Exception("The booking not found");
        log.warn(e.getError());
        return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemRequestNotFoundException.class)
    @ResponseBody
    protected ResponseEntity<Exception> handleItemRequestNotFoundException() {
        Exception e = new Exception("The request not found");
        log.warn(e.getError());
        return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    protected ResponseEntity<Exception> handleMethodArgumentNotValidException() {
        Exception e = new Exception("The object is not valid");
        log.warn(e.getError());
        return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemNotAvailableException.class)
    @ResponseBody
    protected ResponseEntity<Exception> handleItemIsUnavailable() {
        Exception e = new Exception("The item is unavailable");
        log.warn(e.getError());
        return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommentForbiddenException.class)
    @ResponseBody
    protected ResponseEntity<Exception> handleCommentForbidden() {
        Exception e = new Exception("The user cannot write a review for this item");
        log.warn(e.getError());
        return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookingForbiddenException.class)
    @ResponseBody
    protected ResponseEntity<Exception> handleBookingForbiddenException() {
        Exception e = new Exception("The user cannot book his item");
        log.warn(e.getError());
        return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseBody
    protected ResponseEntity<Exception> handleUserAlreadyExistException() {
        Exception e = new Exception("The user is already exist");
        log.warn(e.getError());
        return new ResponseEntity<>(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CommentAlreadyExistException.class)
    @ResponseBody
    protected ResponseEntity<Exception> handleCommentAlreadyExistException() {
        Exception e = new Exception("The comment is already exist");
        log.warn(e.getError());
        return new ResponseEntity<>(e, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseBody
    protected ResponseEntity<Exception> handleItemNotFoundException() {
        Exception e = new Exception("The item not found");
        log.warn(e.getError());
        return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    protected ResponseEntity<Exception> handleUserNotFoundException() {
        Exception e = new Exception("The user not found");
        log.warn(e.getError());
        return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
    }

    @Data
    @AllArgsConstructor
    private static class Exception {
        private String error;
    }
}
