package org.example.backender101homebanking.service;

import lombok.RequiredArgsConstructor;
import org.example.backender101homebanking.dto.ApiResponseDto;
import org.example.backender101homebanking.dto.SignInRequestDto;
import org.example.backender101homebanking.dto.SignInResponseDto;
import org.example.backender101homebanking.dto.SignUpRequestDto;
import org.example.backender101homebanking.exception.RoleNotFoundException;
import org.example.backender101homebanking.exception.UserAlreadyExistsException;
import org.example.backender101homebanking.mapper.UserMapper;
import org.example.backender101homebanking.model.Role;
import org.example.backender101homebanking.model.User;
import org.example.backender101homebanking.security.UserDetailsImpl;
import org.example.backender101homebanking.security.jwt.JwtUtils;
import org.example.backender101homebanking.utils.RoleFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleFactory roleFactory;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<ApiResponseDto<?>> signUpUser(SignUpRequestDto signUpRequestDto)
            throws UserAlreadyExistsException, RoleNotFoundException {
        if (userService.existsByEmail(signUpRequestDto.getEmail())) {
            throw new UserAlreadyExistsException("Registration Failed: Provided email already exists. Try sign in or provide another email.");
        }
        if (userService.existsByUsername(signUpRequestDto.getUsername())) {
            throw new UserAlreadyExistsException("Registration Failed: Provided username already exists. Try sign in or provide another username.");
        }

        User user = createUser(signUpRequestDto);
        userService.addUser(user);
        ApiResponseDto<List<String>> responseDto = new ApiResponseDto<>(
                true,
                "User account has been successfully created!"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    private User createUser(SignUpRequestDto signUpRequestDto) throws RoleNotFoundException {
        User newUser = userMapper.convertToEntity(signUpRequestDto);
        newUser.setPassword(passwordEncoder.encode(signUpRequestDto.getPassword()));
        newUser.setRoles(determineRoles(signUpRequestDto.getRoles()));

        return newUser;
    }

    private Set<Role> determineRoles(Set<String> strRoles) throws RoleNotFoundException {
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            roles.add(roleFactory.getInstance("user"));
        } else {
            for (String role : strRoles) {
                roles.add(roleFactory.getInstance(role));
            }
        }
        return roles;
    }

    /* functionality for user sign-in */
    @Override
    public ResponseEntity<ApiResponseDto<?>> signInUser(SignInRequestDto signInRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequestDto.getEmail(), signInRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        SignInResponseDto signInResponseDto = new SignInResponseDto();
        signInResponseDto.setUsername(userDetails.getUsername());
        signInResponseDto.setEmail(userDetails.getEmail());
        signInResponseDto.setId(userDetails.getId());
        signInResponseDto.setToken(jwt);
        signInResponseDto.setType("Bearer");
        signInResponseDto.setRoles(roles);

        ApiResponseDto<SignInResponseDto> responseDto = new ApiResponseDto<SignInResponseDto>();
        responseDto.setSuccess(true);
        responseDto.setMessage("Sign in successful!");
        responseDto.setResponse(signInResponseDto);

        return ResponseEntity.ok(responseDto);
    }
}
