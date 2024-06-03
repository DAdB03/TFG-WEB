package com.snakernet.registrousuarios;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Representa un usuario en el sistema.
 */
@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String usuario;
    private String nombre;
    private String apellido;
    private String email;
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_role")
    @JsonBackReference
    private Role role;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonManagedReference
    private UserInfo userInfo;

    /**
     * Obtiene la información adicional del usuario.
     *
     * @return la información adicional del usuario
     */
    public UserInfo getUserInfo() {
        return userInfo;
    }

    /**
     * Establece la información adicional del usuario.
     *
     * @param userInfo la nueva información adicional del usuario
     */
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
        userInfo.setUsuario(this);
    }

    /**
     * Obtiene el ID del usuario.
     *
     * @return el ID del usuario
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID del usuario.
     *
     * @param id el nuevo ID del usuario
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return el nombre del usuario
     */
    public String getFirstName() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     *
     * @param nombre el nuevo nombre del usuario
     */
    public void setFirstName(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido del usuario.
     *
     * @return el apellido del usuario
     */
    public String getLastName() {
        return apellido;
    }

    /**
     * Establece el apellido del usuario.
     *
     * @param apellido el nuevo apellido del usuario
     */
    public void setLastName(String apellido) {
        this.apellido = apellido;
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
    }

    /**
     * Obtiene el nombre de usuario.
     *
     * @return el nombre de usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Establece el nombre de usuario.
     *
     * @param usuario el nuevo nombre de usuario
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el rol del usuario.
     *
     * @return el rol del usuario
     */
    public Role getRole() {
        return role;
    }

    /**
     * Establece el rol del usuario.
     *
     * @param role el nuevo rol del usuario
     */
    public void setRole(Role role) {
        this.role = role;
    }
}
