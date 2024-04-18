package com.snakernet.registrousuarios;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF configuration
            .csrf(csrf -> csrf.disable()) // Example to disable CSRF, adjust as necessary for your application

            // Authorization configuration
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/login.html", "/register.html", "/home.html", "/assets/**").permitAll()
                .anyRequest().authenticated())

            // Form login configuration
            .formLogin(form -> form
                .loginPage("/index.html")
                .defaultSuccessUrl("/index.html", true)
                .permitAll())

            // Logout configuration
            .logout(logout -> logout.permitAll());

        return http.build();
    }

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
}
