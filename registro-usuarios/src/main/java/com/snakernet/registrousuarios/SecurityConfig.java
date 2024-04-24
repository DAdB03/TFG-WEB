package com.snakernet.registrousuarios;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				// CORS configuration
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))

				// CSRF configuration
				.csrf(csrf -> csrf.disable()) // Disable CSRF, adjust as necessary for your application

				// Authorization configuration
				.authorizeHttpRequests(authz -> authz
						.requestMatchers("/", "/index.html", "/login.html", "/register.html", "/home.html",
								"/assets/**", "/users/register", "users/login")
						.permitAll() // Ensure all these paths are accessible without authentication
						.anyRequest().authenticated())

				// Form login configuration
				.formLogin(form -> form.loginPage("/login.html") // Set the login page
						.defaultSuccessUrl("/index.html", true) // Redirect to index.html upon successful login
						.permitAll()) // Ensure the login page is accessible without authentication

				// Logout configuration
				.logout(logout -> logout.logoutSuccessUrl("/index.html") // Redirect to index.html on logout
						.permitAll());

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Allow your frontend host
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
		configuration.setAllowCredentials(true); // Important for cookies, authorization headers with HTTPS

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}