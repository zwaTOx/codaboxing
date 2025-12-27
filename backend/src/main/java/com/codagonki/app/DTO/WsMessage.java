package com.codagonki.app.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WsMessage {
    private MessageType type;
    private String content;
    private String sender;
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    public enum MessageType {
        NOTIFICATION, ATTEMPT, SOLVED, GAME_END, LEAVE
    }
}