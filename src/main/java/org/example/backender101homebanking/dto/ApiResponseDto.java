package org.example.backender101homebanking.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto<T> {
    private boolean isSuccess;
    private String message;
    private T response;

    public ApiResponseDto(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.response = null;
    }
}
