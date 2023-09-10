package com.student.controlleradvise;

import com.student.exception.ReceiptNotFoundException;
import com.student.util.CustomResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Exception Occurred {} ", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ReceiptNotFoundException.class)
    public ResponseEntity<CustomResponse> handleReceiptNotFoundException(Exception e) {
        log.error("Exception Occurred {} ", e.getMessage());
        return new ResponseEntity<>(CustomResponse.sendError(e.getMessage()), HttpStatus.NO_CONTENT);
    }

}
