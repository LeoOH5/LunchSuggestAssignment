package com.sparta.lunchsuggestassignment.controller;

import com.sparta.lunchsuggestassignment.dto.LoginRequestDto;
import com.sparta.lunchsuggestassignment.dto.LoginResponseDto;
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
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/reissue")
    public ResponseEntity<LoginResponseDto> reissue(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return ResponseEntity.ok(authService.reissue(refreshToken));
    }
}
