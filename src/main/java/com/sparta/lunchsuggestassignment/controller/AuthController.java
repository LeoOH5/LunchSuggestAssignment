package com.sparta.lunchsuggestassignment.controller;

import com.sparta.lunchsuggestassignment.dto.SignupRequestDto;
import com.sparta.lunchsuggestassignment.dto.SignupResponseDto;
import com.sparta.lunchsuggestassignment.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto, UriComponentsBuilder uriBuilder) {
        SignupResponseDto created = authService.signup(signupRequestDto);

        //Location 헤더를 위한 URI 생성
        URI location = uriBuilder
                .path("/api/users/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        //201 Created 상태 + Location 헤더 + 응답 body 반환
        return ResponseEntity
                .created(location)
                .body(created);
    }
}
