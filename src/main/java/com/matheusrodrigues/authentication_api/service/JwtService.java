package com.matheusrodrigues.authentication_api.service;

import java.sql.Date;

import org.springframework.stereotype.Service;

import com.matheusrodrigues.authentication_api.models.User;
import com.matheusrodrigues.authentication_api.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//Classe transcrita, algoritmo para a geração do Token
@Service
public class JwtService {

    private final String SECRET_KEY = "suaChaveSecretaAqui";

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().getName().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public User getUserFromToken(String token, UserRepository repository) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();
            return repository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Token inválido: usuário não encontrado."));
        } catch (Exception e) {
            throw new IllegalArgumentException("Token inválido ou expirado.");
        }
    }
}