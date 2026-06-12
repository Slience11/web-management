package com.itheima.filter;

import com.itheima.utils.CurrentHolder;
import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter("/*")
public class TokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        //获取请求路径
        String requestURI = req.getRequestURI();
        //判断是否为登录请求，是则放行
        if ( requestURI.contains("/login")){
            log.info("登录请求，放行");
            chain.doFilter(request,response);
            return;
        }

        //获取请求路径的token
        String jwt =req.getHeader("token");
        //判断token是否为空，是则相应401
        if (jwt==null|| jwt.isEmpty()){
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        //判断token是否正确，不正确则相应401
        try {
            Claims claims = JwtUtils.parseJWT(jwt);
            Integer empId = Integer.valueOf(claims.get("id").toString());
            CurrentHolder.setCurrentId(empId);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("token解析失败，返回错误结果");
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        //放行
        log.info("token正确，放行");
        chain.doFilter(request,response);
        CurrentHolder.remove();
    }
}
