package com.marublosso.worktrack.worktrack_backend.interceptor;

import java.util.HashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.Map;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loginUser") == null) {
            // Map으로 데이터 준비
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("success", false);
            resMap.put("redirectUrl", "/worktrack/login");

            // JSON으로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(resMap);

            // 응답 처리
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return false;
        }

        return true;
    }

}
