package com.itheima.filter;

import com.itheima.utils.CurrentHolder;
import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter("/*")
public class TokenFilter implements Filter {

    private static final String TOKEN_HEADER = "token";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (shouldSkip(req)) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = resolveToken(req);
        if (jwt == null || jwt.isBlank()) {
            writeUnauthorized(resp, "未登录或登录已过期");
            return;
        }

        try {
            Claims claims = JwtUtils.parseJWT(jwt);
            CurrentHolder.setCurrentId(Integer.valueOf(claims.get("id").toString()));
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            writeUnauthorized(resp, "登录状态无效，请重新登录");
            CurrentHolder.remove();
            return;
        }

        try {
            chain.doFilter(request, response);
        } finally {
            CurrentHolder.remove();
        }
    }

    private boolean shouldSkip(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return "OPTIONS".equalsIgnoreCase(request.getMethod())
                || "/login".equals(requestURI)
                || requestURI.endsWith("/login")
                || requestURI.startsWith("/upload.html");
    }

    private String resolveToken(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
            return authorization.substring(BEARER_PREFIX.length());
        }
        return request.getHeader(TOKEN_HEADER);
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":0,\"msg\":\"" + message + "\"}");
    }
}
