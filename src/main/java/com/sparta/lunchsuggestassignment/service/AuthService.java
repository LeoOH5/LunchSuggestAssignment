package com.sparta.lunchsuggestassignment.service;

import com.sparta.lunchsuggestassignment.dto.SignupRequestDto;
import com.sparta.lunchsuggestassignment.dto.SignupResponseDto;
import com.sparta.lunchsuggestassignment.entity.User;
import com.sparta.lunchsuggestassignment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    @Transactional
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        String name = signupRequestDto.getName();
        String email = signupRequestDto.getEmail();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // email로 중복 막을 것이기 때문에 이메일만 중복인지 체크
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        User user = User.createUser(email,password,name);
        User saved = userRepository.save(user);

        return new SignupResponseDto(saved.getId(),saved.getEmail(),saved.getName(),saved.getCreatedAt());
    }
}
