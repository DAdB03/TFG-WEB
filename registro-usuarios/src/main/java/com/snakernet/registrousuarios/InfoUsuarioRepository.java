package com.snakernet.registrousuarios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para gestionar la entidad UserInfo.
 */
@Repository
public interface InfoUsuarioRepository extends JpaRepository<UserInfo, Long> {

    /**
     * Encuentra un UserInfo por su ID de usuario.
     *
     * @param usuarioId el ID del usuario
     * @return el UserInfo correspondiente al ID de usuario dado
     */
    UserInfo findByUsuarioId(Long usuarioId);
}
