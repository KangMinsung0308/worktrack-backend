package com.marublosso.worktrack.worktrack_backend.controller.api;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marublosso.worktrack.worktrack_backend.dto.LoginUserDto;
import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;
import com.marublosso.worktrack.worktrack_backend.service.biz.java.errorcheck.ErrorHandler;
import com.marublosso.worktrack.worktrack_backend.service.biz.java.errorcheck.UserChecker;
import com.marublosso.worktrack.worktrack_backend.service.biz.java.errorcheck.WorkTimeChecker;
import com.marublosso.worktrack.worktrack_backend.service.biz.java.features.GetMonthWorkTimeService;
import com.marublosso.worktrack.worktrack_backend.service.biz.java.features.PostWorkTimeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = "*") // <-- 로컬 테스트용: 필요시 특정 origin으로 변경하세요 (지피티가 써준거 이게 먼지 모름 일단 서버 올리면 지울거임)
@RestController
@RequestMapping("/worktrack")
public class WorkTimeController {

    private final PostWorkTimeService workTimeService;
    private final GetMonthWorkTimeService GetMonthWorkTimeService;
    private final WorkTimeChecker workTimeChecker;
    private final ErrorHandler errorHandler;
    // private final UserChecker userChecker;

    public WorkTimeController(PostWorkTimeService workTimeService, WorkTimeChecker workTimeChecker,
            ErrorHandler errorHandler, UserChecker userChecker, GetMonthWorkTimeService GetMonthWorkTimeService) {
        this.workTimeService = workTimeService;
        this.workTimeChecker = workTimeChecker;
        this.errorHandler = errorHandler;
        // this.userChecker = userChecker;
        this.GetMonthWorkTimeService = GetMonthWorkTimeService;
    }

    // 근무시간 DB 기록 (JSON (Input) -> DB(Record))
    @PostMapping("/record")
    public ResponseEntity<?> postWorkTimeControll(@RequestBody WorkTimeRequestDto request, HttpServletRequest sessionRequest) {
        Map<String, Object> response = new HashMap<>();

        // TODO Exception 기반으로 변경하기
        // 출퇴근 시간 유효성 검사
        int timeErrorCode = workTimeChecker.isValidWorkTime(request);
        if (timeErrorCode != 0) {// 에러코드 0 = 정상
            return ResponseEntity
                    .badRequest()
                    .body(errorHandler.handleError(timeErrorCode));
        }

        // 출퇴근 시간 유효성 검사
        // int userErrorCode = userChecker.isValidUser(request);
        // if (userErrorCode != 0) {// 에러코드 0 = 정상
        // return ResponseEntity
        // .badRequest()
        // .body(errorHandler.handleError(userErrorCode));
        // }

        // 세션에서 사용자 정보 가져오기
        HttpSession session = sessionRequest.getSession(false);

        LoginUserDto loginUserDto =(LoginUserDto)session.getAttribute("loginUser");

        // 서비스 호출
        workTimeService.recordWorkTime(request,loginUserDto);

        response.put("success", true);

        return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);
    }

    // 근무시간 조회 (QueryPara(Input) -> Json(Return))
    @GetMapping("/{workDate}")
    public ResponseEntity<?> getWorkTimeControll(
            @PathVariable("workDate") LocalDate workDate,
            HttpServletRequest sessionRequest) 
        {

        // 세션에서 사용자 정보 가져오기
        HttpSession session = sessionRequest.getSession(false);

        LoginUserDto loginUserDto =(LoginUserDto)session.getAttribute("loginUser");

        // 서비스 호출
        List<WorkTimeRequestDto> results = GetMonthWorkTimeService.getWorkTime(workDate,loginUserDto);
        return ResponseEntity.ok(results);
    }
}
