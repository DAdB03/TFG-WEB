package com.snakernet.registrousuarios;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filtro para interceptar las solicitudes HTTP y validar el token JWT.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    private JwtUtil jwtUtil;
    private UserDetailsService userDetailsService;

    /**
     * Constructor para inyectar JwtUtil y UserDetailsService.
     *
     * @param jwtUtil el utilitario para gestionar JWT
     * @param userDetailsService el servicio para cargar detalles del usuario
     */
    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                String username = jwtUtil.extractUsername(token);
                logger.debug("Token JWT recibido para el usuario: {}", username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        logger.debug("Token JWT válido para el usuario: {}", username);
                    }
                }
            } catch (ExpiredJwtException e) {
                logger.warn("Token expirado para el usuario: {}", e.getClaims().getSubject());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
                return;
            } catch (Exception e) {
                logger.error("Error al procesar el token JWT", e);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
