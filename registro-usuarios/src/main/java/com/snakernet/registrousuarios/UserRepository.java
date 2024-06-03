package com.snakernet.registrousuarios;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para gestionar la entidad User.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Encuentra un usuario por su correo electrónico.
     *
     * @param email el correo electrónico del usuario
     * @return el usuario con el correo electrónico dado, o null si no se encuentra
     */
    User findByEmail(String email);
}
