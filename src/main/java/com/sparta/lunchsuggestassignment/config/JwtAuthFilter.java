package com.sparta.lunchsuggestassignment.config;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // 공개 경로
    private static final String[] PUBLIC_URIS = {
            "/api/auth/signup",
            "/api/auth/login",
            "/api/auth/reissue",
            "/health"
    };

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String uri = request.getRequestURI();

        // 공개 경로는 통과
        if (isPublic(uri)) {
            chain.doFilter(request, response);
            return;
        }

        // 보호 경로 -> 헤더에서 토큰 추출
        String bearer = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);

        // 토큰 없음 -> 403
        if (!StringUtils.hasText(bearer) || !bearer.startsWith(JwtUtil.BEARER_PREFIX)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("403 Forbidden");
            return;
        }

        String token = jwtUtil.substringToken(bearer);

        try {
            // 검증 + 파싱
            Jws<Claims> jws = jwtUtil.parse(token);
            String subject = jws.getBody().getSubject();

            // 인증 성공 -> SecurityContext 저장
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(subject, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);

            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("토큰 만료 401 Unauthorized");
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException
                 | IllegalArgumentException | SecurityException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("토큰 손상  401 Unauthorized");
        }
    }

    private boolean isPublic(String uri) {
        for (String open : PUBLIC_URIS) {
            if (uri.startsWith(open)) return true;
        }
        return false;
    }
}

