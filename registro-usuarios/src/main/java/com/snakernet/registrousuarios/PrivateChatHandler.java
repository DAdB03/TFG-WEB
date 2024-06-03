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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maneja las conexiones WebSocket para el chat privado.
 */
@Service
public class PrivateChatHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(PrivateChatHandler.class);

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PrivateChatService privateChatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String fromUser = getUsername(session, "fromUser");
        String toUser = getUsername(session, "toUser");
        sessions.put(fromUser + "-" + toUser, session);

        logger.debug("Conexión establecida: {} -> {}", fromUser, toUser);

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

        logger.debug("Conexión cerrada: {} -> {}", fromUser, toUser);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(payload);
        } catch (Exception e) {
            logger.error("Mensaje mal formado: {}", payload, e);
            String errorMessage = "{\"error\": \"Mensaje mal formado.\"}";
            session.sendMessage(new TextMessage(errorMessage));
            return;
        }

        String fromUser = jsonNode.get("fromUser").asText();
        String content = jsonNode.get("content").asText();
        String toUser = getUsername(session, "toUser");

        logger.debug("Mensaje recibido de {} para {}: {}", fromUser, toUser, content);

        privateChatService.saveMessage(fromUser, toUser, content);

        LocalDateTime now = LocalDateTime.now();
        String formattedMessage = String.format(
            "{\"fromUser\":\"%s\", \"content\":\"%s\", \"timestamp\":\"%s\"}", 
            fromUser, content, now.toString()
        );

        WebSocketSession toSession = sessions.get(toUser + "-" + fromUser);
        if (toSession != null && toSession.isOpen()) {
            toSession.sendMessage(new TextMessage(formattedMessage));
            logger.debug("Mensaje enviado a {}: {}", toUser, formattedMessage);
        }

        session.sendMessage(new TextMessage(formattedMessage));
        logger.debug("Mensaje enviado al remitente {}: {}", fromUser, formattedMessage);
    }

    /**
     * Obtiene el nombre de usuario desde los parámetros de la sesión.
     *
     * @param session la sesión WebSocket
     * @param param el nombre del parámetro que contiene el nombre de usuario
     * @return el nombre de usuario, o null si no se encuentra
     */
    private String getUsername(WebSocketSession session, String param) {
        String query = session.getUri().getQuery();
        for (String paramPair : query.split("&")) {
            String[] pair = paramPair.split("=");
            if (pair.length == 2 && pair[0].equals(param)) {
                return pair[1];
            }
        }
        logger.warn("No se encontró el parámetro {} en la sesión", param);
        return null;
    }
}
