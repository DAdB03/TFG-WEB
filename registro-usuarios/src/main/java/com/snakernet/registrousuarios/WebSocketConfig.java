package com.snakernet.registrousuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private PublicChatHandeler publicChatHandeler;

    @Autowired
    private PrivateChatHandler privateChatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(publicChatHandeler, "/ws/publicappchat").setAllowedOrigins("*");
        registry.addHandler(privateChatHandler, "/ws/privateappchat").setAllowedOrigins("*");
    }
}
