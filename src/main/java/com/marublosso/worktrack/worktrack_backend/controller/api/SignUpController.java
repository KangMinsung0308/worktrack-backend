package com.marublosso.worktrack.worktrack_backend.controller.api;

import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.marublosso.worktrack.worktrack_backend.dto.SignUpRequestDto;
import com.marublosso.worktrack.worktrack_backend.service.biz.java.features.SignUpService;

@RestController
@RequestMapping("/api")
public class SignUpController {

    private final SignUpService signUpService;

    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    /******************************************************************
     * 회원가입 시 이메일 중복 여부를 확인한다.
     *
     * @param sessionRequest   세션 정보
     * @param signUpRequestDto 회원가입 요청 정보
     * @return true : 가입 가능 (ACCEPTED)
     *         false : 이미 존재하는 계정 (CONFLICT)
     *
     * @throws Exception // TODO 미구현
     * 
     * @HttpSession WebConfig에서 세션 유효성 체크
     * 
     */
    @PostMapping("/signUpEmail")
    public ResponseEntity<?> signUpEmail(HttpServletRequest sessionRequest,
            @RequestBody SignUpRequestDto signUpRequestDto) {
        HttpSession session = sessionRequest.getSession(true);

        // TODO : 형식 체커 호출 (유효한 이메일인지 체크)

        // 사용가능한 이메일인지 조회 (true = 사용가능 , false = 이미 존재하는 이메일)
        boolean results;
        results = signUpService.SignUpAccountCheck(signUpRequestDto);

        if (results) {
            //성공
            session.setAttribute("sessionEmail", signUpRequestDto.getUserEmail()); // 세션에 이메일 저장
            session.setAttribute("Session_Online", true);
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "success", true,
                            "message", "사용 가능한 이메일입니다"));
        } else {
            //실패
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "success", false,
                            "message", "이미 존재하는 이메일입니다"));
        }
    }

    /******************************************************************
     * 회원가입 시 유효한 비밀번호인지의 여부를 확인한다.
     *
     * @param sessionRequest   세션 정보
     * @param signUpRequestDto 회원가입 요청 정보
     * @return true : 계정 생성 완료 (CREATED)
     *         false : 유효하지 않은 비밀번호 (CONFLICT)
     *
     * @throws DuplicateKeyException USER 테이블 데이터 레코드 중복
     * 
     * @HttpSession WebConfig에서 세션 유효성 체크
     * 
     */
    @PostMapping("/signUpPassword")
    public ResponseEntity<?> signUpPassword(HttpServletRequest sessionRequest,
            @RequestBody SignUpRequestDto signUpRequestDto) {
        // 세션에서 사용자 정보 가져오기
        HttpSession session = sessionRequest.getSession(false);

        // TODO : 형식 체커 호출 (유효한 비밀번호인지 체크)

        // 이메일, 비밀번호를 유저 테이블에 등록
        String email = (String) session.getAttribute("sessionEmail");
        try {
            signUpService.SignUp(email, signUpRequestDto);
        } catch (Exception e) {
            // 예외 처리
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "success", false,
                            "message", "알수 없는 이유로 회원가입에 실패하였습니다"));
        }

        // 성공
        session.invalidate();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "success", true,
                        "message", "회원가입 성공"));
    }
}
