package com.itheima;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JwtTest {

    private static final String SIGN_KEY = "aXRjYXN0";

    @Test
    public void testGenAndParseJwt() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 10);
        claims.put("username", "itheima");

        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SIGN_KEY)
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + 12 * 3600 * 1000))
                .compact();

        Claims parsedClaims = Jwts.parser()
                .setSigningKey(SIGN_KEY)
                .parseClaimsJws(jwt)
                .getBody();

        assertEquals(10, parsedClaims.get("id", Integer.class));
        assertEquals("itheima", parsedClaims.get("username", String.class));
    }
}
