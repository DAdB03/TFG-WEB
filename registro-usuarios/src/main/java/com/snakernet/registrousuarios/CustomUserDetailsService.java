package com.snakernet.registrousuarios;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servicio personalizado para cargar los detalles de un usuario.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserService userService;

    /**
     * Carga los detalles de un usuario utilizando su nombre de usuario.
     *
     * @param username el nombre de usuario (en este caso, se espera que sea un ID en formato String)
     * @return un objeto UserDetails que contiene la información del usuario
     * @throws UsernameNotFoundException si el usuario no puede ser encontrado o el ID tiene un formato inválido
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Intentando cargar el usuario con username: {}", username);

        Long userId;
        try {
            userId = Long.parseLong(username);  // Intenta convertir el username a Long
        } catch (NumberFormatException e) {
            logger.error("Formato de ID inválido para username: {}", username, e);
            throw new UsernameNotFoundException("Formato de ID inválido: " + username);
        }

        User user = userService.findById(userId).orElseThrow(() -> {
            logger.warn("No se pudo encontrar el usuario con ID: {}", userId);
            return new UsernameNotFoundException("No se pudo encontrar el usuario con ID: " + userId);
        });

        logger.debug("Usuario encontrado: {}", user.getEmail());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), 
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()))
        );
    }
}
