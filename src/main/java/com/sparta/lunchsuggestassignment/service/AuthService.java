package com.sparta.lunchsuggestassignment.service;

import com.sparta.lunchsuggestassignment.config.JwtUtil;
import com.sparta.lunchsuggestassignment.dto.LoginRequestDto;
import com.sparta.lunchsuggestassignment.dto.LoginResponseDto;
import com.sparta.lunchsuggestassignment.dto.SignupRequestDto;
import com.sparta.lunchsuggestassignment.dto.SignupResponseDto;
import com.sparta.lunchsuggestassignment.entity.User;
import com.sparta.lunchsuggestassignment.repository.RefreshTokenStore;
import com.sparta.lunchsuggestassignment.repository.UserRepository;
import io.jsonwebtoken.Claims;
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
    private final JwtUtil jwtUtil;
    private final RefreshTokenStore refreshTokenStore;

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

    // 로그인
    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(loginRequestDto.getPassword(),user.getPassword())) {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtUtil.createToken(user.getEmail());
        String refreshToken = jwtUtil.createRefreshToken(user.getEmail());

        // refreshToken은 서버에 저장
        refreshTokenStore.save(user.getEmail(), refreshToken);

        return new LoginResponseDto(accessToken, refreshToken);
    }

    // 토큰 재발급
    @Transactional
    public LoginResponseDto reissue(String refreshToken) {
        jwtUtil.validateOrThrow(refreshToken);

        Claims claims = jwtUtil.getUserInfoFromToken(refreshToken);
        String email = claims.getSubject();

        // 저장소에 있는 최신 refresh 와 일치하는지 체크
        String saved = refreshTokenStore.get(email);
        if (saved == null || !saved.equals(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 Refresh 토큰입니다.");
        }

        String newAccess = jwtUtil.createToken(email);
        String newRefresh = jwtUtil.createRefreshToken(email);
        refreshTokenStore.save(email, newRefresh);

        return new LoginResponseDto(newAccess, newRefresh);
    }
}
