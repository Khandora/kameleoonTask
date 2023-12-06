package com.kameleoon.test.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterResponseDto {
    private UserDto userDto;
    private String token;
}
