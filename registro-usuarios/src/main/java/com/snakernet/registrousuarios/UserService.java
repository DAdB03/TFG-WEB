package com.snakernet.registrousuarios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

	public User findByEmail(String email) {
		return userRepository.findByEmail(email); // Utiliza el método del repositorio
	}
	
	public Optional<User> findById(Long id) {
	    return userRepository.findById(id);
	}

	public User registerUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword())); // Codifica la contraseña antes de guardarla
		return userRepository.save(user); // Guarda el usuario en la base de datos
	}
	
	public Page<User> listarTodosLosUsuarios(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
	
	public List<User> listarTodosLosUsuarios() {
        return userRepository.findAll();
    }
	
	public void updateUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Role newRole = roleRepository.findByNombre(roleName).orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(newRole);
        userRepository.save(user);
    }
}