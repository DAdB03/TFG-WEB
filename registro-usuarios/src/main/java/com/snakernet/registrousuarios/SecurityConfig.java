package com.snakernet.registrousuarios;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	 @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http, JwtUtil jwtUtil, UserDetailsService userDetailsService) throws Exception {
	        JwtRequestFilter jwtRequestFilter = new JwtRequestFilter(jwtUtil, userDetailsService);
	         
	        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
	            .csrf(csrf -> csrf.disable())
	            .authorizeHttpRequests(authz -> authz
	                .requestMatchers("/", "/index.html", "/login.html", "/register.html", "/assets/**", "/users/**", "/uploads/**", "/table.html", "/profile.html", "/FaQ.html", "/ws/**", "/Chat.html")
	                .permitAll()
	                .anyRequest().authenticated())
	            .formLogin(form -> form.loginPage("/login.html").defaultSuccessUrl("/index.html", true).permitAll())
	            .logout(logout -> logout.logoutSuccessUrl("/index.html").permitAll())
	            .httpBasic(httpBasic -> httpBasic.disable())
	            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

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

// .requestMatchers("/", "/index.html", "/login.html", "/register.html", "/assets/**", "/users/register", "/users/login", "/users/auth/{id}", ,)