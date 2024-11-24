package org.example.task_manager.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.task_manager.models.User;

import java.security.Key;
import java.util.Date;

public class JWTUtil {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generateRefreshToken(User user) {
        long expirationTimeMillis =  7 * 24 * 60 * 60 * 1000;
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTimeMillis);
        return Jwts.builder()
                .setSubject(String.valueOf(user.getUserId()))
                .claim("email", user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    public static String generateAccessToken(User user) {
        long expirationTimeMillis = 15 * 60 * 1000; // 3 минуты
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expirationTimeMillis);
        return Jwts.builder()
                .setSubject(String.valueOf(user.getUserId()))
                .claim("role", user.getRole())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    public static Claims extractClaim(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token.replace("Bearer", ""))
                .getBody();
        return claims;
    }

    public static boolean validateToken(String token) {
        try {
            Claims claim = extractClaim(token);
            return !claim.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
