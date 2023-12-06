package com.kameleoon.test.facade;

import com.kameleoon.test.model.dto.AuthenticationRequestDto;
import com.kameleoon.test.model.dto.RegisterRequestDto;
import com.kameleoon.test.model.dto.RegisterResponseDto;
import com.kameleoon.test.model.dto.UserDto;
import com.kameleoon.test.model.entity.User;
import com.kameleoon.test.security.jwt.JwtTokenProvider;
import com.kameleoon.test.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthenticationFacade {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthenticationFacade(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    public ResponseEntity<String> login(AuthenticationRequestDto requestDto) {
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            Optional<User> user = userService.findByUsername(username);
            if (user.isEmpty()) {
                throw new UsernameNotFoundException("User with username: " + username + " not found");
            }
            String token = jwtTokenProvider.createToken(username, user.get().getRoles());

            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    public ResponseEntity<RegisterResponseDto> register(RegisterRequestDto requestDto) {
        try {
            Optional<User> user = userService.findByUsername(requestDto.getUsername());
            Optional<User> emailEntry = userService.findByEmail(requestDto.getEmail());
            if (user.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists!");
            }
            if (emailEntry.isPresent()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists!");
            }

            User newUser = User.builder().firstName(requestDto.getFirstname()).lastName(requestDto.getLastname()).
                    username(requestDto.getUsername()).email(requestDto.getEmail()).
                    password(requestDto.getPassword()).build();
            User savedUser = userService.register(newUser);

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(savedUser.getUsername(), requestDto.getPassword()));
            String token = jwtTokenProvider.createToken(savedUser.getUsername(), savedUser.getRoles());
            RegisterResponseDto responseDto = new RegisterResponseDto(UserDto.fromUser(savedUser), token);

            return ResponseEntity.ok(responseDto);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Something went wrong!");
        }
    }
}
