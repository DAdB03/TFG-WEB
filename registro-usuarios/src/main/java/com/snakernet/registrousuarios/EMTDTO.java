package com.snakernet.registrousuarios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Transfer Object (DTO) para encapsular los datos de correo electrónico y contraseña.
 */
public class EMTDTO {

    private static final Logger logger = LoggerFactory.getLogger(EMTDTO.class);

    private String email;
    private String password;

    /**
     * Constructor para crear un EMTDTO con email y contraseña.
     *
     * @param email el correo electrónico del usuario
     * @param password la contraseña del usuario
     */
    public EMTDTO(String email, String password) {
        this.email = email;
        this.password = password;
        logger.debug("EMTDTO creado con email: {}", email);
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return el correo electrónico del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param email el nuevo correo electrónico del usuario
     */
    public void setEmail(String email) {
        this.email = email;
        logger.debug("Email actualizado a: {}", email);
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return la contraseña del usuario
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param password la nueva contraseña del usuario
     */
    public void setPassword(String password) {
        this.password = password;
        logger.debug("Password actualizado");
    }
}
