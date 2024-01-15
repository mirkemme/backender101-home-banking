package org.example.backender101homebanking.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResponse {
    private int statusCode;
    private String message;
}