package com.web.swbotv1.Security;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

@Component
public class JwtUtil {
   private final String SECRET_KEY = "esta_es_mi_llave_super_secreta_para_firmar_el_token_123456";
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 4; // 4 horas

    private byte[] getSigningKey() {
        return SECRET_KEY.getBytes();
    }

    // Generar token
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(getSigningKey()))
                .compact();
    }

    // Obtener usuario del token
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Obtener expiración
    public Date extractExpiration(String token) {
        return getClaims(token).getExpiration();
    }

    // Verificar si expiró
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // NUEVO → Validación completa usada por JwtFilter
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Validar token (versión simple)
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(getSigningKey()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}