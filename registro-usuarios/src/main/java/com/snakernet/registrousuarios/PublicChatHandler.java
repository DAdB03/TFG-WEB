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

/**
 * Controlador que maneja las conexiones WebSocket para el chat público.
 * Gestiona el establecimiento y cierre de conexiones, además del envío y recepción de mensajes.
 */
@Service
public class PublicChatHandler extends TextWebSocketHandler {

    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Autowired
    private PublicChatService messageService;

    /**
     * Método llamado después de que una conexión WebSocket se ha establecido.
     * Añade la sesión a la lista de sesiones activas y envía mensajes antiguos al usuario recién conectado.
     * 
     * @param sesión WebSocket que se ha establecido
     * @throws Excepción si ocurre algún error al manejar la conexión
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);

        // Envía mensajes antiguos al usuario recién conectado
        List<PublicMessage> messages = messageService.getAllMessages();
        for (PublicMessage message : messages) {
            String formattedTime = message.getHora().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            session.sendMessage(new TextMessage(message.getUsername() + ": " + message.getContent() + " [" + formattedTime + "]"));
        }
    }

    /**
     * Método llamado una vez se ha cerrado la conexión WebSocket.
     * Elimina la sesión de la lista de sesiones activas.
     * 
     * @param sesión WebSocket que se ha cerrado
     * @param estado de cierre
     * @throws excepción si ocurre algún error al manejar el cierre de la conexión
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    /**
     * Método llamado cuando se recibe un mensaje de texto a través de la conexión WebSocket.
     * Parsea el mensaje, lo guarda en la base de datos y lo reenvía a todos los usuarios conectados.
     * 
     * @param sesión WebSocket que envió el mensaje
     * @param mensaje de texto recibido
     * @throws excepción si ocurre algún error al manejar el mensaje
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        String[] parts = payload.split(": ", 2);
        String username = parts[0];
        String content = parts[1];

        // Guarda mensaje en la base de datos
        messageService.saveMessage(username, content);

        // Envia mensaje a todos los usuarios conectados
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        TextMessage formattedMessage = new TextMessage(username + ": " + content + " [" + formattedTime + "]");
        for (WebSocketSession webSocketSession : sessions) {
            webSocketSession.sendMessage(formattedMessage);
        }
    }
}
