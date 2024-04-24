package com.snakernet.registrousuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
	    System.out.println("Received user: " + user);

	    if (user.getEmail() == null || !user.getEmail().endsWith("@educamadrid.org")) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                             .body("Error en el registro: el correo electrónico debe terminar con @educamadrid.org");
	    }

	    // Check if the user already exists
	    User existingUser = userService.findByEmail(user.getEmail());
	    if (existingUser != null) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                             .body("Usuario ya existe. Por favor, inicie sesión.");
	    }

	    try {
	        userService.registerUser(user); // directly calling the service method without assigning the result
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
        	System.out.println("AAAA");
            return ResponseEntity.ok("Login exitoso. Redireccionando a index.html...");
        } else {
        	System.out.println("bbbbb");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }
    
    
}