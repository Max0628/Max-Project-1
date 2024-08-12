package com.maxchauo.springserver.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class JWTutils {
    private static final long EXPIRE_TIME = 30 * 60 * 1000;

    @Value("${jwt.secret}")
    private String tokenSecret;

    //generate a secret key
    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(tokenSecret.getBytes());
    }

    //use my info encode to generate a JWT token
    public String createJWT(Long id, String name, String email, String provider, String picture) {


        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //storage my data in a Map
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("email", email);
        map.put("provider", provider);
        map.put("picture", picture);

        //start build a JWT token
        String jwt = Jwts.builder().setClaims(map)
                .claim("id", id)
                .setIssuedAt(now)
                .setExpiration(new Date(nowMillis + EXPIRE_TIME))
                .signWith(getSecretKey())
                .compact();
        return jwt;
    }

    //parse JWT methods
    public Claims parseJWT(String jwt) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            return claims;
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("JWT parsing failed.");
        }
    }
}
