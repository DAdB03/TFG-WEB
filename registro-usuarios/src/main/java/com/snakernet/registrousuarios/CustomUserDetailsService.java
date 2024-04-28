package com.snakernet.registrousuarios;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Long userId;
        try {
            userId = Long.parseLong(username);  // Intenta convertir el username a Long
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Formato de ID inválido: " + username);
        }

        User user = userService.findById(userId).orElseThrow(() -> new UsernameNotFoundException("No se pudo encontrar el usuario con ID: " + userId));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));  // Usar ROLE_ como prefijo si estás configurando autorizaciones basadas en roles
    }
}
