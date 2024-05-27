package com.snakernet.registrousuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@SpringBootApplication
@EnableWebSocket
public class WebSocketApp implements WebSocketConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketApp.class, args);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler(), "/publicappchat").setAllowedOrigins("*");
    }

    @Bean
    public PublicChatHandeler chatHandler() {
        return new PublicChatHandeler();
    }

}
