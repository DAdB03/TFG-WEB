package com.snakernet.registrousuarios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para gestionar la entidad Role.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Encuentra un rol por su nombre.
     *
     * @param nombre el nombre del rol
     * @return un Optional que contiene el rol si se encuentra, o vacío si no se encuentra
     */
    Optional<Role> findByNombre(String nombre);
}
