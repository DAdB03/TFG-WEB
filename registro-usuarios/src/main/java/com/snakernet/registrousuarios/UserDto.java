package com.snakernet.registrousuarios;

public class UserDto {
	private String firstName;
    private String lastName;
    private String imageUrl;

    public UserDto(String firstName, String lastName, String imageUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
