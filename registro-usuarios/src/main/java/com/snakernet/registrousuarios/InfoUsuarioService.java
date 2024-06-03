package com.snakernet.registrousuarios;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servicio para gestionar la información adicional del usuario.
 */
@Service
public class InfoUsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(InfoUsuarioService.class);

    private final InfoUsuarioRepository infoUsuarioRepository;

    /**
     * Constructor para inyectar el repositorio InfoUsuarioRepository.
     *
     * @param infoUsuarioRepository el repositorio de información de usuario
     */
    public InfoUsuarioService(InfoUsuarioRepository infoUsuarioRepository) {
        this.infoUsuarioRepository = infoUsuarioRepository;
    }

    /**
     * Obtiene la información adicional del usuario por su ID.
     *
     * @param usuarioId el ID del usuario
     * @return el objeto UserInfo correspondiente al ID de usuario dado
     */
    public UserInfo findByUsuarioId(Long usuarioId) {
        logger.debug("Buscando información del usuario con ID: {}", usuarioId);
        UserInfo userInfo = infoUsuarioRepository.findByUsuarioId(usuarioId);
        if (userInfo != null) {
            logger.debug("Información del usuario encontrada: {}", userInfo);
        } else {
            logger.warn("No se encontró información del usuario para el ID: {}", usuarioId);
        }
        return userInfo;
    }
}
