package com.codagonki.app.DTO.Duel;

import com.codagonki.app.DTO.User.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DuelInfoResponse {
    private Long id;
    private List<UserResponse> participants;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime startTime;
    private Integer problemCount;
    private Long currentUserId;

}
