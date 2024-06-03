package com.snakernet.registrousuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Aplicación principal para configurar y ejecutar la aplicación WebSocket.
 */
@SpringBootApplication
@EnableWebSocket
public class WebSocketApp implements WebSocketConfigurer {

    /**
     * Método principal para ejecutar la aplicación Spring Boot.
     *
     * @param args los argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(WebSocketApp.class, args);
    }

    /**
     * Registra los manejadores de WebSocket.
     *
     * @param registry el registro de manejadores de WebSocket
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler(), "/publicappchat").setAllowedOrigins("*");
    }

    /**
     * Define el bean para el manejador de chat público.
     *
     * @return el manejador de chat público
     */
    @Bean
    public PublicChatHandler chatHandler() {
        return new PublicChatHandler();
    }
}
