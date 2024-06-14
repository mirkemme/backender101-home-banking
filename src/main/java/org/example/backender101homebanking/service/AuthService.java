package org.example.backender101homebanking.service;

import org.example.backender101homebanking.dto.SignInResponseDto;
import org.example.backender101homebanking.dto.SignInRequestDto;
import org.example.backender101homebanking.dto.SignUpRequestDto;
import org.example.backender101homebanking.exception.RoleNotFoundException;
import org.example.backender101homebanking.exception.UserAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    ResponseEntity<SignInResponseDto> signUpUser(SignUpRequestDto signUpRequestDto) throws UserAlreadyExistsException, RoleNotFoundException;
    ResponseEntity<SignInResponseDto> signInUser(SignInRequestDto signInRequestDto);
}
