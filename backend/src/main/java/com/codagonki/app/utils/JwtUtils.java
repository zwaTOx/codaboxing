package com.codagonki.app.utils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.codagonki.app.models.User;
import com.codagonki.app.repositories.UserRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    
    public User getUserFromCookie(HttpServletRequest request) {
        String accessToken = getCookieValue(request, "accessToken");
        if (accessToken == null || accessToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access token не найден в куках");
        }
        
        if (!jwtTokenProvider.validateAccessToken(accessToken)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Некорректный access token");
        }
        
        Claims claims = jwtTokenProvider.getAccessTokenClaims(accessToken);
        Long userId = claims.get("user_id", Long.class);
        
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Пользователь не найден");
        }
        
        return userOptional.get();
    }

    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        String refreshToken = getCookieValue(request, "refreshToken");
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token не найден в куках");
        }
        return refreshToken;
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public void setTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {        
        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(false);
        accessTokenCookie.setSecure(false);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge((int) TimeUnit.MINUTES.toSeconds(30)); 
        
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/"); 
        refreshTokenCookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(7)); 

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
    }
}