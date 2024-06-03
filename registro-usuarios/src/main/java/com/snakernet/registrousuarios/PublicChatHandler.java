package com.snakernet.registrousuarios;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maneja las conexiones WebSocket para el chat público.
 */
@Service
public class PublicChatHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(PublicChatHandler.class);

    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Autowired
    private PublicChatService messageService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        logger.debug("Conexión establecida: {}", session.getId());

        // Enviar mensajes antiguos al usuario recién conectado
        List<PublicMessage> messages = messageService.getAllMessages();
        for (PublicMessage message : messages) {
            String formattedTime = message.getHora().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            session.sendMessage(new TextMessage(message.getUsername() + ": " + message.getContent() + " [" + formattedTime + "]"));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        logger.debug("Conexión cerrada: {}", session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String[] parts = payload.split(": ", 2);
        if (parts.length < 2) {
            logger.warn("Mensaje mal formado recibido: {}", payload);
            session.sendMessage(new TextMessage("Mensaje mal formado."));
            return;
        }

        String username = parts[0];
        String content = parts[1];

        logger.debug("Mensaje recibido de {}: {}", username, content);

        // Guardar mensaje en la base de datos
        messageService.saveMessage(username, content);

        // Enviar mensaje a todos los usuarios conectados
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        TextMessage formattedMessage = new TextMessage(username + ": " + content + " [" + formattedTime + "]");
        for (WebSocketSession webSocketSession : sessions) {
            webSocketSession.sendMessage(formattedMessage);
        }

        logger.debug("Mensaje enviado a todos los usuarios conectados: {}", formattedMessage.getPayload());
    }
}
