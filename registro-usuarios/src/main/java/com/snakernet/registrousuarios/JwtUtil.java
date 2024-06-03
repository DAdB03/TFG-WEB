package com.snakernet.registrousuarios;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilitario para gestionar la generación y validación de tokens JWT.
 */
@Service
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    String secretKey; // Debe ser base64 encoded si es necesario

    /**
     * Genera un token JWT para un usuario dado.
     *
     * @param user el usuario para el cual se generará el token
     * @return el token JWT generado
     */
    public String generateToken(User user) {
        logger.debug("Generando token para el usuario: {}", user.getId());

        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        Claims claims = Jwts.claims().setSubject(user.getId().toString());
        claims.put("role", "ROLE_" + user.getRole().getName());

        String token = Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(new Date())
                   .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas de validez
                   .signWith(key)
                   .compact();

        logger.debug("Token generado: {}", token);
        return token;
    }

    /**
     * Extrae los claims de un token JWT.
     *
     * @param token el token JWT
     * @return los claims extraídos del token
     */
    public Claims extractClaims(String token) {
        logger.debug("Extrayendo claims del token");
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    /**
     * Extrae el nombre de usuario (subject) de un token JWT.
     *
     * @param token el token JWT
     * @return el nombre de usuario extraído del token
     */
    public String extractUsername(String token) {
        logger.debug("Extrayendo nombre de usuario del token");
        return extractClaims(token).getSubject();
    }

    /**
     * Extrae el rol de un token JWT.
     *
     * @param token el token JWT
     * @return el rol extraído del token
     */
    public String extractRole(String token) {
        logger.debug("Extrayendo rol del token");
        return (String) extractClaims(token).get("role");
    }

    /**
     * Verifica si un token JWT ha expirado.
     *
     * @param token el token JWT
     * @return true si el token ha expirado, false en caso contrario
     */
    public boolean isTokenExpired(String token) {
        logger.debug("Verificando si el token ha expirado");
        return extractClaims(token).getExpiration().before(new Date());
    }

    /**
     * Valida un token JWT comparando el nombre de usuario y verificando su expiración.
     *
     * @param token el token JWT
     * @param userDetails los detalles del usuario
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateToken(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
        final String username = extractUsername(token);
        boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        logger.debug("Token válido: {}", isValid);
        return isValid;
    }
}
