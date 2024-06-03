package com.snakernet.registrousuarios;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Representa la información adicional de un usuario en el sistema.
 */
@Entity
@Table(name = "info_usuario")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    @JsonBackReference
    private User usuario;

    @Column(name = "image_url")
    private String imageUrl;

    private String centro;

    private String direccion;

    private String ciudad;

    private String curso;

    /**
     * Constructor por defecto.
     */
    public UserInfo() {
    }

    /**
     * Constructor para crear un UserInfo con detalles específicos.
     *
     * @param imageUrl la URL de la imagen del usuario
     * @param centro el centro del usuario
     * @param direccion la dirección del usuario
     * @param ciudad la ciudad del usuario
     * @param curso el curso del usuario
     */
    public UserInfo(String imageUrl, String centro, String direccion, String ciudad, String curso) {
        this.imageUrl = imageUrl;
        this.centro = centro;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.curso = curso;
    }

    /**
     * Obtiene el ID de la información del usuario.
     *
     * @return el ID de la información del usuario
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID de la información del usuario.
     *
     * @param id el nuevo ID de la información del usuario
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el usuario asociado a esta información.
     *
     * @return el usuario asociado
     */
    public User getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario asociado a esta información.
     *
     * @param usuario el nuevo usuario asociado
     */
    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene la URL de la imagen del usuario.
     *
     * @return la URL de la imagen del usuario
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Establece la URL de la imagen del usuario.
     *
     * @param imageUrl la nueva URL de la imagen del usuario
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Obtiene el centro del usuario.
     *
     * @return el centro del usuario
     */
    public String getCentro() {
        return centro;
    }

    /**
     * Establece el centro del usuario.
     *
     * @param centro el nuevo centro del usuario
     */
    public void setCentro(String centro) {
        this.centro = centro;
    }

    /**
     * Obtiene la dirección del usuario.
     *
     * @return la dirección del usuario
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección del usuario.
     *
     * @param direccion la nueva dirección del usuario
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene la ciudad del usuario.
     *
     * @return la ciudad del usuario
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * Establece la ciudad del usuario.
     *
     * @param ciudad la nueva ciudad del usuario
     */
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * Obtiene el curso del usuario.
     *
     * @return el curso del usuario
     */
    public String getCurso() {
        return curso;
    }

    /**
     * Establece el curso del usuario.
     *
     * @param curso el nuevo curso del usuario
     */
    public void setCurso(String curso) {
        this.curso = curso;
    }
}
