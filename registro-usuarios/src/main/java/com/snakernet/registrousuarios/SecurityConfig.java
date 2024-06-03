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

/**
 * Configuración de seguridad para la aplicación.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura el filtro de seguridad de la aplicación.
     *
     * @param http el objeto HttpSecurity para configurar la seguridad web
     * @param jwtUtil el utilitario para gestionar JWT
     * @param userDetailsService el servicio para cargar detalles del usuario
     * @return el filtro de seguridad configurado
     * @throws Exception si ocurre un error durante la configuración
     */
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

    /**
     * Proporciona un codificador de contraseñas utilizando BCrypt.
     *
     * @return el codificador de contraseñas
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura la fuente de configuración CORS.
     *
     * @return la fuente de configuración CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Permitir tu host de frontend
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true); // Importante para cookies, encabezados de autorización con HTTPS

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
