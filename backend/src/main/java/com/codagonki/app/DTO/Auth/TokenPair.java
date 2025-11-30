package com.codagonki.app.DTO.Auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenPair {
    private String accessToken;
    private String refreshToken;
    private String tokenType;

    public static TokenPair of(String accessToken, String refreshToken) {
        return TokenPair.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .tokenType("Bearer")
            .build();
    }
}