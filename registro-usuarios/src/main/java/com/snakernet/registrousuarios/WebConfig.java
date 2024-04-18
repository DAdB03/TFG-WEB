package com.snakernet.registrousuarios;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class WebConfig {

	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // Permite CORS en todos los endpoints
				.allowedOrigins("http://localhost:5500") // Permite solicitudes CORS de este origen
				.allowedMethods("GET", "POST", "PUT", "DELETE") // Métodos HTTP permitidos
				.allowedHeaders("*") // Permite todas las cabeceras
				.allowCredentials(true);
	}
}
