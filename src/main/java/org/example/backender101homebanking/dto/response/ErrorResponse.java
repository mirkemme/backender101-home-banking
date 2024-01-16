package org.example.backender101homebanking.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorResponse {
    private int statusCode;
    private String message;
}