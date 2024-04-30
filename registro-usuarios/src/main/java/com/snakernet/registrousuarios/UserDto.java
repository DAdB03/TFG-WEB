package com.snakernet.registrousuarios;

public class UserDto {
	private String username;
	private String firstName;
    private String lastName;
    private String imageUrl;
    private String email;

    public UserDto(String username, String firstName, String lastName, String imageUrl, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.email = email;
        this.username = username;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String user) {
		this.username = user;
	}
}
