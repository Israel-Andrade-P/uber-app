package com.zeldev.ride_service.handler;

import com.zeldev.ride_service.exception.RideNotAcceptedException;
import com.zeldev.ride_service.exception.RideNotFoundException;
import com.zeldev.ride_service.exception.RideNotStartedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RideNotFoundException.class)
    public ResponseEntity<String> handler(RideNotFoundException exp) {
        log.error("RideNotFoundException thrown: {}", exp.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(exp.getMessage());
    }

    @ExceptionHandler(RideNotAcceptedException.class)
    public ResponseEntity<String> handler(RideNotAcceptedException exp) {
        log.error("RideNotAcceptedException thrown: {}", exp.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(exp.getMessage());
    }

    @ExceptionHandler(RideNotStartedException.class)
    public ResponseEntity<String> handler(RideNotStartedException exp) {
        log.error("RideNotStartedException thrown: {}", exp.getMessage());
        return ResponseEntity.status(BAD_REQUEST).body(exp.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handler(MethodArgumentNotValidException exp) {
        var errors = new HashMap<String, String>();

        exp.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        return ResponseEntity.status(BAD_REQUEST).body(errors);
    }
}
