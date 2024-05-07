package com.snakernet.registrousuarios;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	private InfoUsuarioService infoUsuarioService;
	@Autowired
	private InfoUsuarioRepository infoUsuarioRepository;
	@Autowired
	private FtpStorageService imageStorageService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		System.out.println("Received user: " + user);
		user.toString();
		if (user.getEmail() == null || !user.getEmail().endsWith("@educamadrid.org")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Error en el registro: el correo electrónico debe terminar con @educamadrid.org");
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

		UserDto userDto = new UserDto(user.getUsuario(), user.getFirstName(), user.getLastName(),
				infoUsuario.getImageUrl(), user.getEmail(), infoUsuario.getCentro(), infoUsuario.getCiudad(),
				infoUsuario.getDireccion());
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
			return ResponseEntity.ok(Collections.singletonMap("message", "Image updated successfully!"));
		} catch (Exception e) {
			System.out.println("Error updating image: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/table/list")
	public ResponseEntity<List<User>> obtenerTodosLosUsuarios() {
		List<User> usuarios = userService.listarTodosLosUsuarios();
		System.out.println("Listar todos");
		System.out.println(usuarios);
		return ResponseEntity.ok(usuarios);
	}
}