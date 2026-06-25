package br.com.sgc.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret:minha-chave-secreta-muito-segura-para-producao-mudarem-em-prod}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;

    // Converte a String secreta em um objeto SecretKey seguro usando o algoritmo HMAC-SHA
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String gerarToken(String username) {
        Date agora = new Date();
        Date expiracao = new Date(agora.getTime() + jwtExpiration);

        // Sintaxe moderna do JJWT: usa .subject(), .issuedAt(), .expiration() e .signWith(key)
        return Jwts.builder()
                .subject(username)
                .issuedAt(agora)
                .expiration(expiracao)
                .signWith(getSigningKey()) // O algoritmo (HS512) é detectado automaticamente pelo tamanho da chave!
                .compact();
    }

    public String extrairUsername(String token) {
        // Sintaxe moderna: Agora usamos Jwts.parser() diretamente, sem o "builder"
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload() // Antigamente era .getBody(), agora é .getPayload()
                .getSubject();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}