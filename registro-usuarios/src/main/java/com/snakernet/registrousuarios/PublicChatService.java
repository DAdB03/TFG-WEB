package com.snakernet.registrousuarios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servicio para manejar la lógica del chat público.
 */
@Service
public class PublicChatService {

    private static final Logger logger = LoggerFactory.getLogger(PublicChatService.class);

    @Autowired
    private PublicChatRepository messageRepository;

    /**
     * Obtiene todos los mensajes del chat público.
     *
     * @return una lista de todos los mensajes públicos
     */
    public List<PublicMessage> getAllMessages() {
        logger.debug("Recuperando todos los mensajes públicos");
        List<PublicMessage> messages = messageRepository.findAll();
        logger.debug("Se recuperaron {} mensajes públicos", messages.size());
        return messages;
    }

    /**
     * Guarda un nuevo mensaje en el repositorio.
     *
     * @param username el nombre de usuario que envía el mensaje
     * @param content el contenido del mensaje
     */
    public void saveMessage(String username, String content) {
        PublicMessage message = new PublicMessage();
        message.setUsername(username);
        message.setContent(content);
        message.setHora(LocalDateTime.now());

        logger.debug("Guardando mensaje de {} con contenido: {}", username, content);
        messageRepository.save(message);
        logger.info("Mensaje guardado exitosamente de {}", username);
    }
}
