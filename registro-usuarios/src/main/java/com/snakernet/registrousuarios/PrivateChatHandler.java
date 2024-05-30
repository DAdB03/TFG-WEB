package com.snakernet.registrousuarios;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PrivateChatHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

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
            String formattedMessage = String.format(
                "{\"fromUser\":\"%s\", \"content\":\"%s\", \"timestamp\":\"%s\"}", 
                message.getFromUser(), message.getContent(), message.getTimestamp().toString()
            );
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

        // Parsear el JSON recibido
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(payload);
        } catch (Exception e) {
            String errorMessage = "{\"error\": \"Mensaje mal formado.\"}";
            session.sendMessage(new TextMessage(errorMessage));
            return;
        }

        String fromUser = jsonNode.get("fromUser").asText();
        String content = jsonNode.get("content").asText();
        String toUser = getUsername(session, "toUser");

        // Guardar mensaje en la base de datos
        privateChatService.saveMessage(fromUser, toUser, content);

        // Obtener el timestamp del mensaje guardado
        LocalDateTime now = LocalDateTime.now();

        // Formatear el mensaje con la fecha y hora
        String formattedMessage = String.format(
            "{\"fromUser\":\"%s\", \"content\":\"%s\", \"timestamp\":\"%s\"}", 
            fromUser, content, now.toString()
        );

        // Enviar mensaje al usuario destinatario si está conectado
        WebSocketSession toSession = sessions.get(toUser + "-" + fromUser);
        if (toSession != null && toSession.isOpen()) {
            toSession.sendMessage(new TextMessage(formattedMessage));
        }

        // Enviar mensaje al remitente para mostrar en su chat
        session.sendMessage(new TextMessage(formattedMessage));
    }

    private String getUsername(WebSocketSession session, String param) {
        String query = session.getUri().getQuery();
        for (String paramPair : query.split("&")) {
            String[] pair = paramPair.split("=");
            if (pair.length == 2 && pair[0].equals(param)) {
                return pair[1];
            }
        }
        return null;
    }
}
