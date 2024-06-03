package com.snakernet.registrousuarios;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

/**
 * Clase que representa la entidad de un PrivateMessage.
 * Se utiliza para mapear la tabla private_message de la base de datos, que almacena mensajes privados.
 * Cada mensaje contiene un ID, el nombre de usuario del remitente, el nombre de usuario del destinatario, el contenido del mensaje y la fecha y hora del envío del mensaje.
 */
@Entity
public class PrivateMessage {

    /**
     * Identificador único del mensaje privado.
     * Este ID se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario de la persona que envía el mensaje.
     */
    private String fromUser;

    /**
     * Nombre de usuario de la persona que recibe el mensaje.
     */
    private String toUser;

    /**
     * Contenido del mensaje privado.
     */
    private String content;

    /**
     * Fecha y hora en la que se envió el mensaje.
     */
    private LocalDateTime timestamp;

    /**
     * Obtiene el identificador del mensaje.
     * 
     * @return ID del mensaje
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador del mensaje.
     * 
     * @param ID a establecer
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de usuario del remitente del mensaje.
     * 
     * @return nombre de usuario del remitente del mensaje
     */
    public String getFromUser() {
        return fromUser;
    }

    /**
     * Establece el nombre de usuario del remitente del mensaje.
     * 
     * @param nombre de usuario del remitente del mensaje a establecer
     */
    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    /**
     * Obtiene el nombre de usuario del destinatario del mensaje.
     * 
     * @return nombre de usuario del destinatario del mensaje
     */
    public String getToUser() {
        return toUser;
    }

    /**
     * Establece el nombre de usuario del destinatario del mensaje.
     * 
     * @param nombre de usuario del destinatario del mensaje a establecer
     */
    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    /**
     * Obtiene el contenido del mensaje.
     * 
     * @return contenido del mensaje
     */
    public String getContent() {
        return content;
    }

    /**
     * Establece el contenido del mensaje.
     * 
     * @param contenido del mensaje a establecer
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Obtiene la marca temporal en la que se envía el mensaje.
     * 
     * @return fecha y hora de envío del mensaje
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Establece la marca temporal en la que se envía el mensaje.
     * 
     * @param fecha y hora temporal de envío del mensaje a establecer
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
