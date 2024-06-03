package com.snakernet.registrousuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repositorio para gestionar la entidad PrivateMessage.
 */
public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {

    /**
     * Encuentra todos los mensajes enviados por un usuario específico a otro usuario específico.
     *
     * @param fromUser el usuario remitente
     * @param toUser el usuario destinatario
     * @return una lista de mensajes privados entre los usuarios especificados
     */
    List<PrivateMessage> findByFromUserAndToUser(String fromUser, String toUser);

    /**
     * Encuentra todos los mensajes recibidos por un usuario específico desde otro usuario específico.
     *
     * @param toUser el usuario destinatario
     * @param fromUser el usuario remitente
     * @return una lista de mensajes privados entre los usuarios especificados
     */
    List<PrivateMessage> findByToUserAndFromUser(String toUser, String fromUser);
}
