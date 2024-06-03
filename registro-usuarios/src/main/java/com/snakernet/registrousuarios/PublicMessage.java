package com.snakernet.registrousuarios;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Clase que representa la entidad de un PublicMessage.
 * Se utiliza para mapear la tabla public_message de la base de datos, que almacena mensajes públicos.
 * Cada mensaje contiene un ID, el nombre de usuario del emisor, el contenido del mensaje y la hora en que se envió el mensaje.
 */
@Entity
public class PublicMessage {
    
    /**
     * Identificador único del mensaje público.
     * Este ID se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Nombre de usuario de la persona que envía el mensaje.
     */
    private String username;
    
    /**
     * Contenido del mensaje público.
     */
    private String content;
    
    /**
     * Hora en que se envía el mensaje.
     */
    private LocalDateTime hora;

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
     * @param id el ID a establecer
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de usuario del emisor del mensaje.
     * 
     * @return nombre de usuario del emisor del mensaje
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario del autor del mensaje.
     * 
     * @param nombre de usuario del emisor del mensaje a establecer
     */
    public void setUsername(String username) {
        this.username = username;
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
     * Obtiene la hora en la que se envía el mensaje.
     * 
     * @return hora de envío del mensaje
     */
    public LocalDateTime getHora() {
        return hora;
    }

    /**
     * Establece la hora en que se envía el mensaje.
     * 
     * @param hora de envío del mensaje a establecer
     */
    public void setHora(LocalDateTime hora) {
        this.hora = hora;
    }
}
