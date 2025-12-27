package com.codagonki.app.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.codagonki.app.DTO.WsMessage;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebSocketService {
    
    private final SimpMessagingTemplate messagingTemplate;
    
    private final Map<Long, String> userSessions = new HashMap<>();
    
    public void addUserSession(Long userId, String sessionId) {
        userSessions.put(userId, sessionId);
    }
    
    public void removeUserSession(Long userId) {
        userSessions.remove(userId);
    }
    
    public void sendToUser(Long userId, WsMessage message) {
        try {
            log.debug("Сообщение отправлено пользователю {}: {}", userId, message);
            messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/messages",
                message
            );
        } catch (Exception e) {
            log.error("Ошибка при отправке сообщения пользователю {}: {}", userId, e.getMessage());
        }
    }
}