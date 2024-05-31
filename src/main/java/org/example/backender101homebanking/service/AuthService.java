package org.example.backender101homebanking.service;

import org.example.backender101homebanking.dto.ApiResponseDto;
import org.example.backender101homebanking.dto.SignInRequestDto;
import org.example.backender101homebanking.dto.SignUpRequestDto;
import org.example.backender101homebanking.exception.RoleNotFoundException;
import org.example.backender101homebanking.exception.UserAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    ResponseEntity<ApiResponseDto<?>> signUpUser(SignUpRequestDto signUpRequestDto) throws UserAlreadyExistsException, RoleNotFoundException;
    ResponseEntity<ApiResponseDto<?>> signInUser(SignInRequestDto signInRequestDto);
}
