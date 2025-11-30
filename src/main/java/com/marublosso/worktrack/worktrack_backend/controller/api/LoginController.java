package com.marublosso.worktrack.worktrack_backend.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.marublosso.worktrack.worktrack_backend.dto.LoginRequestDto;
import com.marublosso.worktrack.worktrack_backend.entity.User;
import com.marublosso.worktrack.worktrack_backend.dto.LoginUserDto;
import com.marublosso.worktrack.worktrack_backend.service.biz.java.features.LoginService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/worktrack")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        LoginUserDto loginUserDto = loginService.login(loginRequestDto);
        if (loginUserDto != null) {
            request.getSession(true).setAttribute("loginUser", loginUserDto);
            response.put("success", true);
            response.put("redirectUrl", "/worktrack");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        } else {
            response.put("success", false);
            response.put("message", "로그인 실패");
            response.put("redirectUrl", "/worktrack/login");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        response.put("success", true);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);    
    }
}
