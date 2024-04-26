package com.snakernet.registrousuarios;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {

	@Value("${jwt.secret}")
	String secretKey; // Debe ser base64 encoded si es necesario

	public String generateToken(User user) {
	    // Convertir la clave secreta codificada en Base64 a una clave de firma Key
	    Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

	    // Preparar los claims del token
	    Claims claims = Jwts.claims().setSubject(user.getId().toString());
	    claims.put("roleID", user.getId_role()); // Almacenar el ID del rol en el token

	    // Construir y retornar el token JWT
	    return Jwts.builder()
	               .setClaims(claims) // Asignar los claims personalizados
	               .setIssuedAt(new Date()) // Marca de tiempo de cuándo se emitió el token
	               .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas de validez
	               .signWith(key) // Firmar el token con la clave HMAC
	               .compact();
	}

	public Claims extractClaims(String token) {
		Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public String extractUsername(String token) {
		return extractClaims(token).getSubject();
	}

	public boolean isTokenExpired(String token) {
		return extractClaims(token).getExpiration().before(new Date());
	}

	public boolean validateToken(String token, org.springframework.security.core.userdetails.UserDetails userDetails) {
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}
}
