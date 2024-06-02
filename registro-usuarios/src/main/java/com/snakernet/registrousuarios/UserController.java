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

@RestController
@RequestMapping("/users")
public class UserController {

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

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
	    System.out.println("Received user: " + user);
	    user.toString();

	    if (user.getEmail() == null || !user.getEmail().endsWith("@educa.madrid.org")) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body("Error en el registro: el correo electrónico debe terminar con @educa.madrid.org");
	    }

	    // Validar contraseña
	    String password = user.getPassword();
	    if (!isValidPassword(password)) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body("Error en el registro: la contraseña debe contener al menos un número, un carácter especial, una letra mayúscula y tener al menos 8 caracteres de longitud");
	    }

	    // Check if the user already exists
	    User existingUser = userService.findByEmail(user.getEmail());
	    if (existingUser != null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario ya existe. Por favor, inicie sesión.");
	    }

	    try {
	        userService.registerUser(user);
	        return ResponseEntity.ok("Registro exitoso");
	    } catch (Exception e) {
	        e.printStackTrace();
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


	@PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        User user = userService.findByEmail(loginRequest.getEmail());
        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user);
            System.out.println("Generated Token: " + token);
            return ResponseEntity.ok(Collections.singletonMap("jwtToken", token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o Contraseña Incorrectos");
        }
    }

	@GetMapping("/auth/{id}")
    @PreAuthorize("isAuthenticated()") // Asegura que solo usuarios autenticados puedan acceder
    public ResponseEntity<?> getUserInfo(@PathVariable Long id) {
        User user = userService.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + id));

        UserInfo infoUsuario = infoUsuarioService.findByUsuarioId(id);

        UserDto userDto = new UserDto(user.getId() , user.getUsuario(), user.getFirstName(), user.getLastName(), infoUsuario.getImageUrl(), user.getEmail(),
        		user.getUserInfo().getCentro(), user.getUserInfo().getCiudad(), user.getUserInfo().getDireccion(), user.getRole().getName(), user.getUserInfo().getCurso());
        return ResponseEntity.ok(userDto);
    }

	@PostMapping("/auth/update-image/{userId}")
	public ResponseEntity<?> updateUserImage(@PathVariable Long userId, @RequestParam("image") MultipartFile file) {
		try {
			System.out.println("Image Call");
			String imageUrl = imageStorageService.storeFile(file); // Almacena la imagen usando SMB
			UserInfo userInfo = infoUsuarioRepository.findByUsuarioId(userId);
			userInfo.setImageUrl(imageUrl); // Actualiza la URL de la imagen en la base de datos
			infoUsuarioRepository.save(userInfo); // Guarda los cambios en la base de datos
			return ResponseEntity.ok(userInfo);
		} catch (Exception e) {
			 System.out.println("Error updating image: " + e.getMessage());
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating image: " + e.getMessage());
		}
	}

	@GetMapping("/table/list")
	public ResponseEntity<List<UserDto>> obtenerTodosLosUsuarios(
	        @RequestParam(defaultValue = "0") int page,  // Página actual, comenzando desde 0
	        @RequestParam(defaultValue = "5") int pageSize) {  // Cantidad de elementos por página

	    Pageable pageable = PageRequest.of(page, pageSize);
	    Page<User> usuarios = userService.listarTodosLosUsuarios(pageable);
	    
	    List<UserDto> userDTOs = usuarios.getContent().stream().map(UserDto::new).collect(Collectors.toList());
	    return ResponseEntity.ok(userDTOs);
	}
	
	@GetMapping("/contact/list")
	public ResponseEntity<List<UserDto>> contactos() {  
	    List<User> usuarios = userService.listarTodosLosUsuarios();
	    
	    // Convertir la lista de usuarios a DTOs
	    List<UserDto> userDTOs = usuarios.stream().map(UserDto::new).collect(Collectors.toList());
	    return ResponseEntity.ok(userDTOs);
	}
	
	@GetMapping("/auth/emt")
    public ResponseEntity<EMTDTO> EMT() {  
        String email = "diego.alonso@baanaloo.com";
        String password = "Chathub24";

        EMTDTO response = new EMTDTO(email, password);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/info/roles")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }
    
    @PutMapping("/role/sw")
    public ResponseEntity<?> updateUserRole(@RequestParam Long userId, @RequestBody RoleUpdateRequest roleUpdateRequest) {
        try {
            userService.updateUserRole(userId, roleUpdateRequest.getRoleName());
            return ResponseEntity.ok("Role updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating role: " + e.getMessage());
        }
    }
}