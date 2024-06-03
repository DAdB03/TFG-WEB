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

/**
 * Controlador que maneja las conexiones WebSocket para el chat privado.
 * Gestiona el establecimiento y cierre de conexiones, así como el envío y recepción de mensajes.
 */
@Service
public class PrivateChatHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PrivateChatService privateChatService;

    /**
     * Método llamado después de que una conexión WebSocket se ha establecido.
     * Recupera los mensajes antiguos entre los usuarios y los envía a la sesión.
     * 
     * @param sesión WebSocket que se ha establecido
     * @throws exceopción por si ocurre algún error al manejar la conexión
     */
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

    /**
     * Método llamado una vez se ha cerrado la conexión WebSocket.
     * Elimina la sesión.
     * 
     * @param sesión WebSocket que se ha cerrado
     * @param estado de cierre
     * @throws excepción si ocurre algún error al manejar el cierre de la conexión
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String fromUser = getUsername(session, "fromUser");
        String toUser = getUsername(session, "toUser");
        sessions.remove(fromUser + "-" + toUser);
    }

    /**
     * Método llamado cuando se recibe un mensaje de texto a través de la conexión WebSocket.
     * Parsea el mensaje, lo guarda en la base de datos y lo reenvía a los usuarios correspondientes.
     * 
     * @param sesión WebSocket que envió el mensaje
     * @param mensaje de texto recibido
     * @throws excepción si ocurre algún error al manejar el mensaje
     */
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

    /**
     * Obtiene el nombre de usuario de los parámetros de la sesión WebSocket.
     * 
     * @param sesión WebSocket
     * @param nombre del parámetro a buscar ("fromUser" o "toUser")
     * @return el nombre de usuario correspondiente al parámetro, o null si no se encuentra
     */
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
