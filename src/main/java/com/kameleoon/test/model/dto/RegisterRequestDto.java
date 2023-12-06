package com.kameleoon.test.model.dto;

import lombok.Data;

@Data
public class RegisterRequestDto {
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
