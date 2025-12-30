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

        String uri = request.getRequestURI();
        boolean isApi = uri.startsWith("/api/");

        // html 페이지 요청이 아닌 경우에는 세션 체크를 하지 않음
        if (!isApi) {
            return true;
        }

        boolean isOnline = session != null
                && Boolean.TRUE.equals(session.getAttribute("Session_Online"));

        if (!isOnline) {
            // Map으로 데이터 준비
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("success", false);
            resMap.put("message", "세션이 만료되었습니다");
            resMap.put("redirectUrl", "/login");

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
