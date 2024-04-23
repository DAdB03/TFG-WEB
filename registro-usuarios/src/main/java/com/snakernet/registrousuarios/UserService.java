package com.snakernet.registrousuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public User registerUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword())); // Codifica la contraseña antes de guardarla
		return userRepository.save(user); // Guarda el usuario en la base de datos
	}
}
