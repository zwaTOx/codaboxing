package com.codagonki.app.DTO.User;

import com.codagonki.app.models.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private long userId;
    private String email;
    private String role;
    private String nickname;

    public UserResponse(User user) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.nickname = user.getNickname();
    }
}