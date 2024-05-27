package com.snakernet.registrousuarios;

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
            session.sendMessage(new TextMessage(message.getUsername() + ": " + message.getContent()));
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

        // Enviar mensaje a todos los usuarios conectados excepto el remitente
        for (WebSocketSession webSocketSession : sessions) {
            if (!webSocketSession.equals(session)) {
                webSocketSession.sendMessage(message);
            }
        }
    }
}
