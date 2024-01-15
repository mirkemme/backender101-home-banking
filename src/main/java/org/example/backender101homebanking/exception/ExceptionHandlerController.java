package org.example.backender101homebanking.exception;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler({ResourceNotFoundException.class, BadRequestException.class, InternalServerErrorException.class})
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        HttpStatus status;
        if (e instanceof ResourceNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (e instanceof BadRequestException) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ErrorResponse errorResponse = new ErrorResponse(status.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, status);
    }
}
