package com.codagonki.app.DTO.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String tokenType;
    
    public static TokenResponse of(String accessToken) {
        return TokenResponse.builder()
            .accessToken(accessToken)
            .tokenType("Bearer")
            .build();
    }
}