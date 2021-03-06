package com.test.skblab.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.AbstractMap;
import java.util.Map;

/**
 * @author Alexander Zubkov
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<Map.Entry<String, String>> handle(UserExistsException exception) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new AbstractMap.SimpleEntry<>("message", exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<Map.Entry<String, String>> handle(Exception exception) {
        log.error("Unable to process this request", exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new AbstractMap.SimpleEntry<>("message", "Unable to process this request"));
    }

}
