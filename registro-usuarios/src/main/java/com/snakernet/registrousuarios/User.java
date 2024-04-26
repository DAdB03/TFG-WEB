package com.snakernet.registrousuarios;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nombre;
	private String apellido;
	private String email;
	private String password;
	private Integer id_role;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return nombre;
	}
	public void setFirstName(String nombre) {
		this.nombre = nombre;
	}
	public String getLastName() {
		return apellido;
	}
	public void setLastName(String apellido) {
		this.apellido = apellido;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getId_role() {
		return id_role;
	}
	public void setId_role(Integer id_role) {
		this.id_role = id_role;
	}
	
}
