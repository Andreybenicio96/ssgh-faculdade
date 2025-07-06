package com.vital_plus.sistema_hospitalar.configuracao;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {
    private static final String SECRET_KEY = "chave-super-secreta-vitalplus-2025";
    private static final long EXPIRATION_TIME = 86400000; // 1 dia

    private Key getChave() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String gerarToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getChave(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getChave())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extrairEmail(String token) {
        return getClaims(token).getSubject();
    }

    public String extrairRole(String token) {
        return getClaims(token).get("role", String.class);
    }
}
