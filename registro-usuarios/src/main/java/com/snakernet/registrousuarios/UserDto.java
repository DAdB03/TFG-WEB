package com.snakernet.registrousuarios;

public class UserDto {
    private String username;
    private String firstName;
    private String lastName;
    private String imageUrl;
    private String email;
    private String address;
    private String centro;
    private String city;
    private String roleName;

    public UserDto(String username, String firstName, String lastName, String imageUrl, String email, String centro, String ciudad, String address, String roleName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.email = email;
        this.centro = centro;
        this.city = ciudad;
        this.address = address;
        this.roleName = roleName;
    }

    public UserDto(User user) {
        this.username = user.getUsuario();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.imageUrl = user.getUserInfo() != null ? user.getUserInfo().getImageUrl() : null;
        this.email = user.getEmail();
        this.centro = user.getUserInfo() != null ? user.getUserInfo().getCentro() : null;
        this.city = user.getUserInfo() != null ? user.getUserInfo().getCiudad() : null;
        this.address = user.getUserInfo() != null ? user.getUserInfo().getDireccion() : null;
        this.roleName = user.getRole().getName();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCentro() {
        return centro;
    }

    public void setCentro(String centro) {
        this.centro = centro;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
