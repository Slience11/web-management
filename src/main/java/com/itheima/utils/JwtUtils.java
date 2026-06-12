package com.itheima.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtils {

    private static final String SIGN_KEY = System.getenv().getOrDefault("JWT_SIGN_KEY", "change-me-in-env");
    private static final Long EXPIRE = 43200000L;

    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, SIGN_KEY)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .compact();
    }

    public static Claims parseJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(SIGN_KEY)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
