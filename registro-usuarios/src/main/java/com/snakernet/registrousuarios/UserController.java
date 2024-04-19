package com.snakernet.registrousuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        System.out.println("Received user: " + user);

        if (user.getEmail() == null || !user.getEmail().endsWith("@educamadrid.org")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body("Error en el registro: el correo electrónico debe terminar con @educamandid.org");
        }

        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok("Registro exitoso");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error en el registro: " + e.getMessage());
        }
    }
}