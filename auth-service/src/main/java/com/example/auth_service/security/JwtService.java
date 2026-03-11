package com.example.auth_service.security;


import com.example.auth_service.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY="parasbramhankarinojri1234456abcdefghijklmnopqrstuvwxyz";
    private final SecretKey key= Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(User user){
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("id",user.getId())
                .claim("username",user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(key)
                .compact();
    }

    public boolean isValid(String token){
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


}
