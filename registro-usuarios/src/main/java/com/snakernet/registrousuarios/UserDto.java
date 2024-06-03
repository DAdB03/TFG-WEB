package com.snakernet.registrousuarios;

/**
 * Data Transfer Object (DTO) para la entidad User.
 */
public class UserDto {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String email;
    private String address;
    private String centro;
    private String city;
    private String roleName;
    private String curso;

    /**
     * Constructor para crear un UserDto con los detalles del usuario.
     *
     * @param id el ID del usuario
     * @param username el nombre de usuario
     * @param firstName el nombre del usuario
     * @param lastName el apellido del usuario
     * @param imageUrl la URL de la imagen del usuario
     * @param email el correo electrónico del usuario
     * @param centro el centro del usuario
     * @param ciudad la ciudad del usuario
     * @param address la dirección del usuario
     * @param roleName el nombre del rol del usuario
     * @param curso el curso del usuario
     */
    public UserDto(Long id, String username, String firstName, String lastName, String imageUrl, String email, String centro, String ciudad, String address, String roleName, String curso) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.email = email;
        this.centro = centro;
        this.city = ciudad;
        this.address = address;
        this.roleName = roleName;
        this.curso = curso;
    }

    /**
     * Constructor para crear un UserDto a partir de una entidad User.
     *
     * @param user la entidad User
     */
    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsuario();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.imageUrl = user.getUserInfo() != null ? user.getUserInfo().getImageUrl() : null;
        this.email = user.getEmail();
        this.centro = user.getUserInfo() != null ? user.getUserInfo().getCentro() : null;
        this.city = user.getUserInfo() != null ? user.getUserInfo().getCiudad() : null;
        this.address = user.getUserInfo() != null ? user.getUserInfo().getDireccion() : null;
        this.roleName = user.getRole().getName();
        this.curso = user.getUserInfo() != null ? user.getUserInfo().getCurso() : null;
    }

    /**
     * Obtiene el nombre de usuario.
     *
     * @return el nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario.
     *
     * @param username el nuevo nombre de usuario
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return el nombre del usuario
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Establece el nombre del usuario.
     *
     * @param firstName el nuevo nombre del usuario
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Obtiene el apellido del usuario.
     *
     * @return el apellido del usuario
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Establece el apellido del usuario.
     *
     * @param lastName el nuevo apellido del usuario
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
     * Obtiene la dirección del usuario.
     *
     * @return la dirección del usuario
     */
    public String getAddress() {
        return address;
    }

    /**
     * Establece la dirección del usuario.
     *
     * @param address la nueva dirección del usuario
     */
    public void setAddress(String address) {
        this.address = address;
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
     * Obtiene la ciudad del usuario.
     *
     * @return la ciudad del usuario
     */
    public String getCity() {
        return city;
    }

    /**
     * Establece la ciudad del usuario.
     *
     * @param city la nueva ciudad del usuario
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Obtiene el nombre del rol del usuario.
     *
     * @return el nombre del rol del usuario
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Establece el nombre del rol del usuario.
     *
     * @param roleName el nuevo nombre del rol del usuario
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
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
}
