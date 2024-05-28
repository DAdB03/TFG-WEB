package com.snakernet.registrousuarios;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Service
public class PrivateChatHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Autowired
    private PrivateChatService privateChatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String fromUser = getUsername(session, "fromUser");
        String toUser = getUsername(session, "toUser");
        sessions.put(fromUser + "-" + toUser, session);

        // Recuperar mensajes antiguos entre los usuarios
        List<PrivateMessage> oldMessages = privateChatService.getMessagesBetweenUsers(fromUser, toUser);
        for (PrivateMessage message : oldMessages) {
            String formattedTime = message.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            String formattedMessage = message.getFromUser() + ": " + message.getContent() + " [" + formattedTime + "]";
            session.sendMessage(new TextMessage(formattedMessage));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String fromUser = getUsername(session, "fromUser");
        String toUser = getUsername(session, "toUser");
        sessions.remove(fromUser + "-" + toUser);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String[] parts = payload.split(": ", 2);
        String fromUser = parts[0];
        String content = parts[1];
        String toUser = getUsername(session, "toUser");

        // Guardar mensaje en la base de datos
        privateChatService.saveMessage(fromUser, toUser, content);

        // Enviar mensaje al usuario destinatario si está conectado
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        TextMessage formattedMessage = new TextMessage(fromUser + ": " + content + " [" + formattedTime + "]");

        WebSocketSession toSession = sessions.get(toUser + "-" + fromUser);
        if (toSession != null && toSession.isOpen()) {
            toSession.sendMessage(formattedMessage);
        }

        // Enviar mensaje al remitente para mostrar en su chat
        session.sendMessage(formattedMessage);
    }

    private String getUsername(WebSocketSession session, String param) {
        return session.getUri().getQuery().split("&")[param.equals("fromUser") ? 0 : 1].split("=")[1];
    }
}
