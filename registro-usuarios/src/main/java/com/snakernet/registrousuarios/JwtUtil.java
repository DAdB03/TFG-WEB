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

	public String generateToken(String username) {
		Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
		return Jwts.builder().setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas de validez
				.signWith(key).compact();
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
