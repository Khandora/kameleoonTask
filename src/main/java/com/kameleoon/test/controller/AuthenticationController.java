package com.kameleoon.test.controller;

import com.kameleoon.test.facade.AuthenticationFacade;
import com.kameleoon.test.model.dto.AuthenticationRequestDto;
import com.kameleoon.test.model.dto.RegisterRequestDto;
import com.kameleoon.test.model.dto.RegisterResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationFacade authenticationFacade;

    public AuthenticationController(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthenticationRequestDto requestDto) {
        return authenticationFacade.login(requestDto);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterRequestDto requestDto) {
        return authenticationFacade.register(requestDto);
    }
}
