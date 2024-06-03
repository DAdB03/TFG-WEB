package com.snakernet.registrousuarios;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servicio para manejar la lógica del chat privado.
 */
@Service
public class PrivateChatService {

    private static final Logger logger = LoggerFactory.getLogger(PrivateChatService.class);

    @Autowired
    private PrivateMessageRepository privateMessageRepository;

    /**
     * Obtiene los mensajes entre dos usuarios.
     *
     * @param fromUser el usuario remitente
     * @param toUser el usuario destinatario
     * @return lista de mensajes ordenados por timestamp
     */
    public List<PrivateMessage> getMessagesBetweenUsers(String fromUser, String toUser) {
        logger.debug("Recuperando mensajes entre {} y {}", fromUser, toUser);
        
        List<PrivateMessage> messages = privateMessageRepository.findByFromUserAndToUser(fromUser, toUser);
        messages.addAll(privateMessageRepository.findByToUserAndFromUser(fromUser, toUser));
        
        // Ordenar los mensajes por timestamp
        List<PrivateMessage> sortedMessages = messages.stream()
                       .sorted((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()))
                       .collect(Collectors.toList());

        logger.debug("Se recuperaron {} mensajes entre {} y {}", sortedMessages.size(), fromUser, toUser);
        return sortedMessages;
    }

    /**
     * Guarda un nuevo mensaje en el repositorio.
     *
     * @param fromUser el usuario remitente
     * @param toUser el usuario destinatario
     * @param content el contenido del mensaje
     */
    public void saveMessage(String fromUser, String toUser, String content) {
        PrivateMessage message = new PrivateMessage();
        message.setFromUser(fromUser);
        message.setToUser(toUser);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        logger.debug("Guardando mensaje de {} para {} con contenido: {}", fromUser, toUser, content);
        privateMessageRepository.save(message);
        logger.info("Mensaje guardado exitosamente de {} para {}", fromUser, toUser);
    }
}
