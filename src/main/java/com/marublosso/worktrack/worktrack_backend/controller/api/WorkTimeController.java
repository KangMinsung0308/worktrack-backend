package com.marublosso.worktrack.worktrack_backend.controller.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marublosso.worktrack.worktrack_backend.dto.LoginUserDto;
import com.marublosso.worktrack.worktrack_backend.dto.PutWorktimeRequestDto;
import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;
import com.marublosso.worktrack.worktrack_backend.exception.InvalidWorkTimeException;
import com.marublosso.worktrack.worktrack_backend.service.biz.java.features.GetMonthWorkTimeService;
import com.marublosso.worktrack.worktrack_backend.service.biz.java.features.PutWorkTimeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
public class WorkTimeController {

    private final GetMonthWorkTimeService GetMonthWorkTimeService;
    private final PutWorkTimeService putWorkTimeService;

    public WorkTimeController( GetMonthWorkTimeService GetMonthWorkTimeService,
            PutWorkTimeService putWorkTimeService) {
        this.GetMonthWorkTimeService = GetMonthWorkTimeService;
        this.putWorkTimeService = putWorkTimeService;
    }

    // 근무시간 조회 (QueryPara(Input) -> Json(Return))
    @GetMapping("/{workDate}")
    public ResponseEntity<?> getWorkTimeControll(
            @PathVariable("workDate") LocalDate workDate,
            HttpServletRequest sessionRequest) {

        // 세션에서 사용자 정보 가져오기
        HttpSession session = sessionRequest.getSession(false);

        LoginUserDto loginUserDto = (LoginUserDto) session.getAttribute("loginUser");

        // 서비스 호출
        List<WorkTimeRequestDto> results = GetMonthWorkTimeService.getWorkTime(workDate, loginUserDto);
        return ResponseEntity.ok(results);
    }

    // 근무시간 등록, 변경 (QueryPara(Input) -> Json(Return))
    @PutMapping("/put/worktime")
    public ResponseEntity<?> putWorkTimeControll(@RequestBody PutWorktimeRequestDto request,
            HttpServletRequest sessionRequest) {

        // 세션에서 사용자 정보 가져오기
        HttpSession session = sessionRequest.getSession(false);

        LoginUserDto loginUserDto = (LoginUserDto) session.getAttribute("loginUser");

        try {
            putWorkTimeService.UpdateWorkTime(request, loginUserDto);
            // 성공
            return ResponseEntity
                    .status(HttpStatus.ACCEPTED).contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "success", true,
                            "message", "근무시간이 성공적으로 등록되었습니다"));
        } catch (DataAccessException e) {
            // DB 처리 중 에러
            return ResponseEntity
                    .status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "success", false,
                            "message", "등록 처리에 실패하였습니다. 관리자에게 문의해주세요."));
        } catch (InvalidWorkTimeException e) {
            // 시간 유효성 검사 에러
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("success", false, "message", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("success", false, "message", "알 수 없는 오류가 발생했습니다."));
        }

    }
}
