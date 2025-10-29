package com.sparta.lunchsuggestassignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SignupResponseDto {
    private Long id;
    private String email;
    private String name;
    private LocalDateTime createdAt;
}
