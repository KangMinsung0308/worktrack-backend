package com.marublosso.worktrack.worktrack_backend.controller.api;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marublosso.worktrack.worktrack_backend.dto.LoginUserDto;
import com.marublosso.worktrack.worktrack_backend.dto.ProfileRequestDto;
import com.marublosso.worktrack.worktrack_backend.service.biz.java.features.UpdateProfileService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class UpdateProfileController {

    private final UpdateProfileService updateProfileService;

    public UpdateProfileController(UpdateProfileService updateProfileService) {
        this.updateProfileService = updateProfileService;
    }

    @PostMapping("/updateProfile")
    public ResponseEntity<?> updateProfile(HttpServletRequest sessionRequest,
            @RequestBody ProfileRequestDto profileRequestDto) {

        HttpSession session = sessionRequest.getSession(false);

        LoginUserDto loginUserDto = (LoginUserDto) session.getAttribute("loginUser");

        // 프로필 업데이트 서비스 호출
        updateProfileService.updateProfile(profileRequestDto, loginUserDto);

        // 성공
        loginUserDto.setUsername(profileRequestDto.getName());
        loginUserDto.setDept(profileRequestDto.getDept());
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "success", true,
                        "message", "프로필 변경에 성공하였습니다"));

    }
}
