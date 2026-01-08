package com.example.Globalin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket 설정 클래스
 * STOMP 프로토콜을 사용한 실시간 채팅 구현
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 메시지 브로커 설정
     * - /topic: 구독 경로 (클라이언트가 메시지를 받을 때)
     * - /app: 애플리케이션 prefix (클라이언트가 메시지를 보낼 때)
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지 구독 경로 설정 (서버 → 클라이언트)
        config.enableSimpleBroker("/topic");

        // 클라이언트가 메시지를 보낼 때 사용할 prefix (클라이언트 → 서버)
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * WebSocket 엔드포인트 등록
     * - /ws-chat: WebSocket 연결 엔드포인트
     * - SockJS: WebSocket을 지원하지 않는 브라우저를 위한 폴백 옵션
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat")
                .setAllowedOrigins("http://localhost:3000")  // React 프론트엔드 허용
                .withSockJS();  // SockJS 폴백 활성화
    }
}
