package me.n0rdy.hackattic.exception;

import me.n0rdy.hackattic.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<RestResponse> handle(TaskRestException exception) {
        logger.error("REST exception occurred: ", exception);
        return ResponseEntity
                       .status(exception.getHttpStatusCode())
                       .body(RestResponse.error(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<RestResponse> handle(EmptyResponseBodyException exception) {
        logger.error("Empty response body exception occurred: ", exception);
        return ResponseEntity
                       .status(HttpStatus.INTERNAL_SERVER_ERROR)
                       .body(RestResponse.error("Hackattic response body is empty"));
    }

    @ExceptionHandler
    public ResponseEntity<RestResponse> handle(GeneralTaskException exception) {
        logger.error("Task exception occurred: ", exception);
        return ResponseEntity
                       .status(HttpStatus.INTERNAL_SERVER_ERROR)
                       .body(RestResponse.error(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<RestResponse> handle(Exception exception) {
        logger.error("Server exception occurred: ", exception);
        return ResponseEntity
                       .status(HttpStatus.INTERNAL_SERVER_ERROR)
                       .body(RestResponse.error(exception.getMessage()));
    }
}
