package com.snakernet.registrousuarios;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador para gestionar los usuarios.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private InfoUsuarioRepository infoUsuarioRepository;
    @Autowired
    private FtpStorageService imageStorageService;
    @Autowired
    private InfoUsuarioService infoUsuarioService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Registra un nuevo usuario.
     *
     * @param user el usuario a registrar
     * @return la respuesta HTTP
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        logger.debug("Received user: {}", user);
        
        if (user.getEmail() == null || !user.getEmail().endsWith("@educa.madrid.org")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error en el registro: el correo electrónico debe terminar con @educa.madrid.org");
        }

        String password = user.getPassword();
        if (!isValidPassword(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error en el registro: la contraseña debe contener al menos un número, un carácter especial, una letra mayúscula y tener al menos 8 caracteres de longitud");
        }

        User existingUser = userService.findByEmail(user.getEmail());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario ya existe. Por favor, inicie sesión.");
        }

        try {
            userService.registerUser(user);
            return ResponseEntity.ok("Registro exitoso");
        } catch (Exception e) {
            logger.error("Error en el registro: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en el registro: " + e.getMessage());
        }
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasNumber = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecialChar = password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));
        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);

        return hasNumber && hasSpecialChar && hasUppercase;
    }

    /**
     * Inicia sesión de un usuario.
     *
     * @param loginRequest la solicitud de inicio de sesión
     * @return la respuesta HTTP con el token JWT
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        User user = userService.findByEmail(loginRequest.getEmail());
        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user);
            logger.debug("Generated Token: {}", token);
            return ResponseEntity.ok(Collections.singletonMap("jwtToken", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o Contraseña Incorrectos");
        }
    }

    /**
     * Obtiene la información de un usuario autenticado.
     *
     * @param id el ID del usuario
     * @return la respuesta HTTP con la información del usuario
     */
    @GetMapping("/auth/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserInfo(@PathVariable Long id) {
        User user = userService.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));

        UserInfo infoUsuario = infoUsuarioService.findByUsuarioId(id);

        UserDto userDto = new UserDto(user.getId(), user.getUsuario(), user.getFirstName(), user.getLastName(),
                infoUsuario.getImageUrl(), user.getEmail(), user.getUserInfo().getCentro(), user.getUserInfo().getCiudad(),
                user.getUserInfo().getDireccion(), user.getRole().getName(), user.getUserInfo().getCurso());
        return ResponseEntity.ok(userDto);
    }

    /**
     * Actualiza la imagen de un usuario.
     *
     * @param userId el ID del usuario
     * @param file el archivo de imagen
     * @return la respuesta HTTP con la información del usuario
     */
    @PostMapping("/auth/update-image/{userId}")
    public ResponseEntity<?> updateUserImage(@PathVariable Long userId, @RequestParam("image") MultipartFile file) {
        try {
            logger.debug("Image Call");
            String imageUrl = imageStorageService.storeFile(file);
            UserInfo userInfo = infoUsuarioRepository.findByUsuarioId(userId);
            userInfo.setImageUrl(imageUrl);
            infoUsuarioRepository.save(userInfo);
            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            logger.error("Error updating image: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating image: " + e.getMessage());
        }
    }

    /**
     * Obtiene todos los usuarios en una lista paginada.
     *
     * @param page la página actual, comenzando desde 0
     * @param pageSize la cantidad de elementos por página
     * @return la respuesta HTTP con la lista de usuarios
     */
    @GetMapping("/table/list")
    public ResponseEntity<List<UserDto>> obtenerTodosLosUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize) {

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<User> usuarios = userService.listarTodosLosUsuarios(pageable);

        List<UserDto> userDTOs = usuarios.getContent().stream().map(UserDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    /**
     * Obtiene todos los contactos en una lista.
     *
     * @return la respuesta HTTP con la lista de contactos
     */
    @GetMapping("/contact/list")
    public ResponseEntity<List<UserDto>> contactos() {
        List<User> usuarios = userService.listarTodosLosUsuarios();

        List<UserDto> userDTOs = usuarios.stream().map(UserDto::new).collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    /**
     * Obtiene las credenciales EMT.
     *
     * @return la respuesta HTTP con las credenciales EMT
     */
    @GetMapping("/auth/emt")
    public ResponseEntity<EMTDTO> EMT() {
        String email = "diego.alonso@baanaloo.com";
        String password = "Chathub24";

        EMTDTO response = new EMTDTO(email, password);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene todos los roles en forma de DTO.
     *
     * @return la respuesta HTTP con la lista de roles
     */
    @GetMapping("/info/roles")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * Actualiza el rol de un usuario.
     *
     * @param userId el ID del usuario
     * @param roleUpdateRequest la solicitud de actualización del rol
     * @return la respuesta HTTP
     */
    @PutMapping("/role/sw")
    public ResponseEntity<?> updateUserRole(@RequestParam Long userId, @RequestBody RoleUpdateRequest roleUpdateRequest) {
        try {
            userService.updateUserRole(userId, roleUpdateRequest.getRoleName());
            return ResponseEntity.ok("Role updated successfully");
        } catch (Exception e) {
            logger.error("Error updating role: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating role: " + e.getMessage());
        }
    }
}
