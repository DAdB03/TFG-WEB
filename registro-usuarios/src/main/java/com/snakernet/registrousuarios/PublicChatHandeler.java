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

@Service
public class PublicChatHandeler extends TextWebSocketHandler {

    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Autowired
    private PublicChatService messageService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);

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
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String[] parts = payload.split(": ", 2);
        String username = parts[0];
        String content = parts[1];

        // Guardar mensaje en la base de datos
        messageService.saveMessage(username, content);

        // Enviar mensaje a todos los usuarios conectados
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        TextMessage formattedMessage = new TextMessage(username + ": " + content + " [" + formattedTime + "]");
        for (WebSocketSession webSocketSession : sessions) {
            webSocketSession.sendMessage(formattedMessage);
        }
    }
}
