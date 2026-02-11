package com.app.telemetria.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.app.telemetria.entity.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET =
            "CHAVE_SUPER_SECRETA_256_BITS_CHAVE_SUPER_SECRETA_256_BITS";

    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 24h

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(Usuario usuario) {

        return Jwts.builder()
                .setSubject(usuario.getLogin())
                .claim("role", usuario.getPerfil().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .after(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
