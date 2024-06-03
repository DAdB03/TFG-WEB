package com.snakernet.registrousuarios;

/**
 * Data Transfer Object (DTO) para la entidad Role.
 */
public class RoleDTO {

    private Long id;
    private String nombre;

    /**
     * Constructor para crear un RoleDTO con el ID y el nombre del rol.
     *
     * @param id el ID del rol
     * @param nombre el nombre del rol
     */
    public RoleDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /**
     * Obtiene el ID del rol.
     *
     * @return el ID del rol
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID del rol.
     *
     * @param id el nuevo ID del rol
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del rol.
     *
     * @return el nombre del rol
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del rol.
     *
     * @param nombre el nuevo nombre del rol
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
