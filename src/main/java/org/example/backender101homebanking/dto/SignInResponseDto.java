package org.example.backender101homebanking.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponseDto {
    private boolean isSuccess;
    private String message;
    private Object response;

    public SignInResponseDto(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.response = null;
    }
}
