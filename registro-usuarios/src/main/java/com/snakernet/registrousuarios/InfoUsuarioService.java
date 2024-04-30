package com.snakernet.registrousuarios;

import org.springframework.stereotype.Service;

@Service
public class InfoUsuarioService {

    private final InfoUsuarioRepository infoUsuarioRepository;

    public InfoUsuarioService(InfoUsuarioRepository infoUsuarioRepository) {
        this.infoUsuarioRepository = infoUsuarioRepository;
    }

    // Método para obtener información adicional del usuario por su ID
    public UserInfo findByUsuarioId(Long usuarioId) {
        return infoUsuarioRepository.findByUsuarioId(usuarioId);
    }
}
