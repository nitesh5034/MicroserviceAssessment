package com.campusbot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.campusbot.dto.botresponse;

import java.net.http.HttpTimeoutException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpTimeoutException.class)
    public ResponseEntity<botresponse> handleTimeoutException(HttpTimeoutException ex) {

        ex.printStackTrace();

        botresponse errorPayload = new botresponse(
                "The AI server is taking too long to respond. Please try again later.",
                "TIMEOUT_FAILURE"
        );

        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(errorPayload);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<botresponse> handleGeneralException(Exception ex) {

        // VERY IMPORTANT
        ex.printStackTrace();

        botresponse errorPayload = new botresponse(
                ex.getMessage(),
                "SYSTEM_ERROR"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorPayload);
    }
}