package com.snakernet.registrousuarios;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Representa un mensaje público en el chat.
 */
@Entity
public class PublicMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String username;
    private String content;
    private LocalDateTime hora; // Campo para la hora del mensaje

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
     * Obtiene el nombre de usuario que envió el mensaje.
     *
     * @return el nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario que envió el mensaje.
     *
     * @param username el nuevo nombre de usuario
     */
    public void setUsername(String username) {
        this.username = username;
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
     * Obtiene la hora en que se envió el mensaje.
     *
     * @return la hora del mensaje
     */
    public LocalDateTime getHora() {
        return hora;
    }

    /**
     * Establece la hora en que se envió el mensaje.
     *
     * @param hora la nueva hora del mensaje
     */
    public void setHora(LocalDateTime hora) {
        this.hora = hora;
    }
}
