package com.codagonki.app.controllers.v1;

import com.codagonki.app.DTO.Auth.LoginRequest;
import com.codagonki.app.DTO.Auth.SignupRequest;
import com.codagonki.app.DTO.Auth.TokenResponse;
import com.codagonki.app.DTO.User.UserResponse;
import com.codagonki.app.services.AuthService;
import com.codagonki.app.services.UserService;
import com.codagonki.app.utils.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        UserResponse user = userService.registerUser(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest,
                                                HttpServletResponse response) { 
        TokenResponse tokenResponse = userService.authenticateUser(loginRequest);
        jwtUtils.setTokenCookies(response, tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());
        return ResponseEntity.ok()
            .header("alg", "HS256")
            .header("typ", "JWT")
            .body(tokenResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshTokens(HttpServletRequest request, HttpServletResponse response) { 
        TokenResponse tokenResponse = authService.refreshTokens(request);
        jwtUtils.setTokenCookies(response, tokenResponse.getAccessToken(), tokenResponse.getRefreshToken());
        return ResponseEntity.ok()
            .header("alg", "HS256")
            .header("typ", "JWT")
            .body(tokenResponse);
        }
}