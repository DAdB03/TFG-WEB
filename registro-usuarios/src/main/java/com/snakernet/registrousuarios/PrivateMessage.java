package com.snakernet.registrousuarios;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

/**
 * Representa un mensaje privado entre dos usuarios.
 */
@Entity
public class PrivateMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String fromUser;
    private String toUser;
    private String content;
    private LocalDateTime timestamp;

    /**
     * Obtiene el ID del mensaje.
     *
     * @return el ID del mensaje
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID del mensaje.
     *
     * @param id el nuevo ID del mensaje
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el usuario remitente del mensaje.
     *
     * @return el usuario remitente
     */
    public String getFromUser() {
        return fromUser;
    }

    /**
     * Establece el usuario remitente del mensaje.
     *
     * @param fromUser el nuevo usuario remitente
     */
    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    /**
     * Obtiene el usuario destinatario del mensaje.
     *
     * @return el usuario destinatario
     */
    public String getToUser() {
        return toUser;
    }

    /**
     * Establece el usuario destinatario del mensaje.
     *
     * @param toUser el nuevo usuario destinatario
     */
    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    /**
     * Obtiene el contenido del mensaje.
     *
     * @return el contenido del mensaje
     */
    public String getContent() {
        return content;
    }

    /**
     * Establece el contenido del mensaje.
     *
     * @param content el nuevo contenido del mensaje
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Obtiene el timestamp del mensaje.
     *
     * @return el timestamp del mensaje
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Establece el timestamp del mensaje.
     *
     * @param timestamp el nuevo timestamp del mensaje
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
