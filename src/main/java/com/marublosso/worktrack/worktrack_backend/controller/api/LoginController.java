package com.marublosso.worktrack.worktrack_backend.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marublosso.worktrack.worktrack_backend.dto.LoginRequestDto;
import com.marublosso.worktrack.worktrack_backend.service.biz.java.features.LoginService;
import com.marublosso.worktrack.worktrack_backend.dto.SessionUserProfile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        SessionUserProfile profilesUserDto = loginService.login(loginRequestDto);
        Long id = profilesUserDto != null ? profilesUserDto.getId() : null;
        String name = profilesUserDto != null ? profilesUserDto.getName() : null;
        String dept = profilesUserDto != null ? profilesUserDto.getDept() : null;

        if (id != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("Id", id);
            session.setAttribute("Name", name);
            session.setAttribute("Dept", dept);
            session.setAttribute("Session_Online", true);

            response.put("success", true);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
        } else {
            response.put("success", false);
            response.put("message", "아이디 또는 비밀번호가 올바르지 않습니다.");
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
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
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }    
}
