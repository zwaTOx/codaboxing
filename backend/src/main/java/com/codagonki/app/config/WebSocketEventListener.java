package com.codagonki.app.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.codagonki.app.DTO.WsMessage;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = "Yasha";
        if (username != null) {
            log.info("user disconnected: {}", username);
            var chatMessage = WsMessage.builder()
                    .type(WsMessage.MessageType.LEAVE)
                    .sender(username)
                    .build();
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }

}