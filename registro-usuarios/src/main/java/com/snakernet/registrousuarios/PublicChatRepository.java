package com.snakernet.registrousuarios;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para gestionar la entidad PublicMessage.
 */
public interface PublicChatRepository extends JpaRepository<PublicMessage, Long> {
    // Métodos adicionales de consulta personalizados pueden ser añadidos aquí
}
