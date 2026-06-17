package com.crypto.portfolio.tracker.crypto_portfolio_tracker.security;

import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET = "crypto-portfolio-tracker-super-secret-key-256bit";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private static final long  EXPIRATION_TIME= 60 * 60 * 1000; // 1 hour

    public String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValidToken(String token, String email){
        try{
            return (extractEmail(token).equals(email) && !isTokenExpire(token));
        } catch (Exception e) {
            return false;
        }
    }

    public String extractEmail(String token) { return getClaims(token).getSubject(); }

    public boolean isTokenExpire(String token){
        return getClaims(token).getExpiration().before(new Date());
    }

    public Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
