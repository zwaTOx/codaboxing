package com.codagonki.app.services;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.codagonki.app.DTO.Auth.LoginRequest;
import com.codagonki.app.DTO.Auth.SignupRequest;
import com.codagonki.app.DTO.Auth.TokenPair;
import com.codagonki.app.DTO.User.UserResponse;
import com.codagonki.app.models.User;
import com.codagonki.app.repositories.UserRepository;
import com.codagonki.app.utils.JwtTokenProvider;
import com.codagonki.app.utils.JwtUtils;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserResponse registerUser(SignupRequest signupRequest) {
        if (!signupRequest.getPassword().equals(signupRequest.getVerifyPassword())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "Пароли не совпадают"
            );
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT, 
                "Пользователь с таким e-mail уже существует. Попробуйте снова."
            );
        }
        User user = User.builder()
            .email(signupRequest.getEmail())
            .hashedPassword(passwordEncoder.encode(signupRequest.getPassword()))
            .nickname(signupRequest.getNickname())
            .role("USER")
            .build();
        User savedUser = userRepository.save(user);
        return UserResponse.builder()
            .userId(savedUser.getId())
            .email(savedUser.getEmail())
            .role(savedUser.getRole())
            .build();
    }
    
    public TokenPair authenticateUser(LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Введен неправильний e-mail или пароль."
            );
        }
        User user = userOptional.get();
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getHashedPassword())) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Введен неправильний e-mail или пароль."
            );
        }
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());
        return TokenPair.of(accessToken, refreshToken);
        }

    public TokenPair refreshTokens(HttpServletRequest request) {
        String refreshToken = jwtUtils.getRefreshTokenFromCookie(request);
        jwtTokenProvider.validateRefreshToken(refreshToken);
        Claims claims = jwtTokenProvider.getRefreshTokenClaims(refreshToken);
        Long userId = claims.get("user_id", Long.class);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не найден"));
        String newAccessToken = jwtTokenProvider.generateAccessToken(user.getId(), user.getRole());
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user.getId());
        return TokenPair.of(newAccessToken, newRefreshToken);
    }
    
    public void logoutUser(HttpServletResponse response){
        jwtUtils.clearTokenCookies(response);
    }
}
