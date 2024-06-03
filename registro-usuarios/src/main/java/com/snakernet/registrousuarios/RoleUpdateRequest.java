package com.snakernet.registrousuarios;

/**
 * Representa una solicitud para actualizar el nombre de un rol.
 */
public class RoleUpdateRequest {

    private String roleName;

    /**
     * Obtiene el nombre del rol.
     *
     * @return el nombre del rol
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Establece el nombre del rol.
     *
     * @param roleName el nuevo nombre del rol
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
