package org.example.backender101homebanking.api;

import org.apache.coyote.BadRequestException;
import org.example.backender101homebanking.dto.ApiResponseDto;
import org.example.backender101homebanking.dto.error.ErrorResponse;
import org.example.backender101homebanking.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        return buildErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        return buildErrorResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleInternalServerErrorException(InternalServerErrorException e) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleInsufficientBalanceException(InsufficientBalanceException e) {
        return buildErrorResponse(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<?>> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception) {

        List<String> errorMessage = new ArrayList<>();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorMessage.add(error.getDefaultMessage());
        });

        ApiResponseDto<List<String>> responseDto = new ApiResponseDto<>(
                false,
                "Registration Failed: Please provide valid data.",
                errorMessage
        );

        return ResponseEntity
                .badRequest()
                .body(responseDto);
    }

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponseDto<?>> UserAlreadyExistsExceptionHandler(UserAlreadyExistsException exception) {

        ApiResponseDto<List<String>> responseDto = new ApiResponseDto<>(
                false,
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(responseDto);
    }

    @ExceptionHandler(value = RoleNotFoundException.class)
    public ResponseEntity<ApiResponseDto<?>> RoleNotFoundExceptionHandler(RoleNotFoundException exception) {

        ApiResponseDto<List<String>> responseDto = new ApiResponseDto<>(
                false,
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(responseDto);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception e, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), e.getMessage());
        return new ResponseEntity<>(errorResponse, status);
    }
}
