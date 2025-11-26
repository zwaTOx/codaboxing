package com.codagonki.app.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.codagonki.app.DTO.Auth.TokenResponse;
import com.codagonki.app.models.User;
import com.codagonki.app.repositories.UserRepository;
import com.codagonki.app.utils.JwtTokenProvider;
import com.codagonki.app.utils.JwtUtils;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;

    public TokenResponse refreshTokens(HttpServletRequest request) {
        String refreshToken = jwtUtils.getRefreshTokenFromCookie(request);
        jwtTokenProvider.validateRefreshToken(refreshToken);
        Claims claims = jwtTokenProvider.getRefreshTokenClaims(refreshToken);
        Long userId = claims.get("user_id", Long.class);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не найден"));
        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getRole());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getId());
        return TokenResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken)
            .build();
    }
}
