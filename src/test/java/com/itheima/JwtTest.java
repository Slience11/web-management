package com.itheima;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    @Test
    public void testGenJwt() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", 10);
        claims.put("username", "itheima");

        String jwt = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, "aXRjYXN0")//设置签名算法
                .addClaims(claims)//设置自定义信息
                .setExpiration(new Date(System.currentTimeMillis() + 12 * 3600 * 1000))//设置过期时间
                .compact();//生成jwt

        System.out.println(jwt);
    }


    @Test
    public void testParseJwt() {
        Claims claims = Jwts.parser().setSigningKey("aXRjYXN0")
                .parseClaimsJws("eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MTAsInVzZXJuYW1lIjoiaXRoZWltYSIsImV4cCI6MTc3NzAwMjc5OX0.O3Gw8m8-AYsA1-G9zPIc8o23rKo3kIE6oeWyDtZ2_TQ")
                .getBody();
        System.out.println(claims);
    }
}
