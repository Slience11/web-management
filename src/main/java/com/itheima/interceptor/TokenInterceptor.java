package com.itheima.interceptor;

import com.itheima.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取请求路径
        String requestURI = request.getRequestURI();
        //判断是否为登录请求，是则放行
        if ( requestURI.contains("/login")){
            log.info("登录请求，放行");
            return true;
        }

        //获取请求路径的token
        String jwt =request.getHeader("token");
        //判断token是否为空，是则相应401
        if (jwt==null|| jwt.isEmpty()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        //判断token是否正确，不正确则相应401
        try {
            JwtUtils.parseJWT(jwt);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("token解析失败，返回错误结果");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        //放行
        log.info("token正确，放行");
        return true;
    }
}
