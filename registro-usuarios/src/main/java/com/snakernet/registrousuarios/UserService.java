package com.snakernet.registrousuarios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servicio para gestionar la lógica relacionada con los usuarios.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * Encuentra un usuario por su correo electrónico.
     *
     * @param email el correo electrónico del usuario
     * @return el usuario con el correo electrónico dado, o null si no se encuentra
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Encuentra un usuario por su ID.
     *
     * @param id el ID del usuario
     * @return un Optional que contiene el usuario si se encuentra, o vacío si no se encuentra
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Registra un nuevo usuario.
     *
     * @param user el usuario a registrar
     * @return el usuario registrado
     */
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Lista todos los usuarios de manera paginada.
     *
     * @param pageable la información de la paginación
     * @return una página de usuarios
     */
    public Page<User> listarTodosLosUsuarios(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * Lista todos los usuarios.
     *
     * @return una lista de todos los usuarios
     */
    public List<User> listarTodosLosUsuarios() {
        return userRepository.findAll();
    }

    /**
     * Actualiza el rol de un usuario.
     *
     * @param userId el ID del usuario
     * @param roleName el nuevo nombre del rol
     */
    public void updateUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Role newRole = roleRepository.findByNombre(roleName).orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(newRole);
        userRepository.save(user);
    }
}
