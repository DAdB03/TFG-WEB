package com.snakernet.registrousuarios;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Representa un rol en el sistema.
 */
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @OneToMany(mappedBy = "role")
    @JsonManagedReference
    private Set<User> users;

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
    public String getName() {
        return nombre;
    }

    /**
     * Establece el nombre del rol.
     *
     * @param name el nuevo nombre del rol
     */
    public void setName(String name) {
        this.nombre = name;
    }

    /**
     * Obtiene los usuarios asociados a este rol.
     *
     * @return el conjunto de usuarios
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Establece los usuarios asociados a este rol.
     *
     * @param users el nuevo conjunto de usuarios
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
