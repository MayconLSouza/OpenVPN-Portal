package com.painelvpn.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private Key getSigningKey() {
        if (secretKey == null || secretKey.trim().isEmpty()) {
            logger.error("JWT secret key is null or empty!");
            throw new IllegalStateException("JWT secret key cannot be null or empty");
        }
        
        logger.debug("JWT secret key length: {} bytes", secretKey.getBytes(StandardCharsets.UTF_8).length);
        
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            logger.error("JWT secret key is too short! Length: {} bytes", keyBytes.length);
            throw new IllegalStateException("JWT secret key must be at least 32 bytes long");
        }
        
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractFuncionarioId(String token) {
        try {
            String funcionarioId = extractClaim(token, claims -> claims.get("funcionarioId", String.class));
            logger.debug("ID do funcionário extraído do token: {}", funcionarioId);
            return funcionarioId;
        } catch (Exception e) {
            logger.error("Erro ao extrair ID do funcionário do token", e);
            return null;
        }
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(UserDetails userDetails, String funcionarioId) {
        if (funcionarioId == null || funcionarioId.trim().isEmpty()) {
            logger.error("Tentativa de gerar token com ID do funcionário nulo ou vazio");
            throw new IllegalArgumentException("ID do funcionário não pode ser nulo ou vazio");
        }

        logger.debug("Gerando token JWT para usuário: {}, ID do funcionário: {}", 
            userDetails.getUsername(), funcionarioId);

        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());
        claims.put("funcionarioId", funcionarioId);
        
        String token = createToken(claims, userDetails.getUsername());
        logger.debug("Token JWT gerado com sucesso");
        
        return token;
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
            logger.debug("Validação do token JWT - Usuário: {}, Token válido: {}", username, isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("Erro ao validar token JWT", e);
            return false;
        }
    }
} 