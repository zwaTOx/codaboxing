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
    
    private String getTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    public User getUserFromToken(HttpServletRequest request) {
        String accessToken = getTokenFromHeader(request);
        if (accessToken == null || accessToken.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access token не найден в заголовке Authorization");
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

    public void setTokenCookies(HttpServletResponse response, String refreshToken) {        
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/"); 
        refreshTokenCookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(7)); 
        refreshTokenCookie.setAttribute("SameSite", "Lax");
        response.addCookie(refreshTokenCookie);
    }

    public void clearTokenCookies(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refreshToken", "");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/api/auth/refresh");
        refreshTokenCookie.setMaxAge(0); 
        response.addCookie(refreshTokenCookie);
}
}