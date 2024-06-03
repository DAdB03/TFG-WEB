package com.snakernet.registrousuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Configuración de WebSocket para la aplicación.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private PublicChatHandler publicChatHandler;

    @Autowired
    private PrivateChatHandler privateChatHandler;

    /**
     * Registra los manejadores de WebSocket.
     *
     * @param registry el registro de manejadores de WebSocket
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(publicChatHandler, "/ws/publicappchat").setAllowedOrigins("*");
        registry.addHandler(privateChatHandler, "/ws/privateappchat").setAllowedOrigins("*");
    }
}
