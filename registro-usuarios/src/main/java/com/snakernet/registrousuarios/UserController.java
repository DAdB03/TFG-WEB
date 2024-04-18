package com.snakernet.registrousuarios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@CrossOrigin(origins = "http://localhost:5500")
	@PostMapping("/register")
	public RedirectView registerUser(@RequestBody User user, HttpServletRequest request) {
	    userService.registerUser(user);
	    // Construye la URL de redirección
	    String contextPath = request.getContextPath();
	    return new RedirectView(contextPath + "/login.html");
	}

}
