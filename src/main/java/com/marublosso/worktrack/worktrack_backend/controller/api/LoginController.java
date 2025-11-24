package com.marublosso.worktrack.worktrack_backend.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.marublosso.worktrack.worktrack_backend.dto.LoginUserDto;
import com.marublosso.worktrack.worktrack_backend.entity.User;
import com.marublosso.worktrack.worktrack_backend.service.biz.java.features.LoginService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/worktrack")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/worktrack/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDto loginUserDto, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        User user = loginService.login(loginUserDto);
        if (user != null) {
            request.getSession(true).setAttribute("loginUser", user);
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


}
