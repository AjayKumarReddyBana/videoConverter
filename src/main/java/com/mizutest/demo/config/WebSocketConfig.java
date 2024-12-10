package com.mizutest.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple in-memory message broker
        config.enableSimpleBroker("/topic"); // Clients will subscribe to topics prefixed with /topic
        config.setApplicationDestinationPrefixes("/api/demo"); // Prefix for messages sent from clients
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register a WebSocket endpoint for clients to connect to
        registry.addEndpoint("/api/demo/ws-progress") // Endpoint URL for WebSocket connections
                .setAllowedOriginPatterns("*")       // Allow connections from all origins
                .withSockJS();              // Fallback to SockJS if WebSocket is unavailable
    }
}
