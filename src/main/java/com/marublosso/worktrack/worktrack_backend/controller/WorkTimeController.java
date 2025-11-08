package com.marublosso.worktrack.worktrack_backend.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;
import com.marublosso.worktrack.worktrack_backend.service.biz.WorkTimeService;
import com.marublosso.worktrack.worktrack_backend.service.checker.errorcheck.ErrorHandler;
import com.marublosso.worktrack.worktrack_backend.service.checker.errorcheck.WorkTimeChecker;


@CrossOrigin(origins = "*") // <-- 로컬 테스트용: 필요시 특정 origin으로 변경하세요 (지피티가 써준거 이게 먼지 모름 일단 서버 올리면 지울거임)
@RestController
@RequestMapping("/worktime")
public class WorkTimeController {

    private final WorkTimeService workTimeService;
    private final WorkTimeChecker workTimeChecker;
    private final ErrorHandler errorHandler;

    public WorkTimeController(WorkTimeService workTimeService, WorkTimeChecker workTimeChecker,ErrorHandler errorHandler) {
        this.workTimeService = workTimeService;
        this.workTimeChecker = workTimeChecker;
        this.errorHandler = errorHandler;
    }
    
    //근무시간 DB 기록 (JSON (Input) -> DB(Record))
    @PostMapping("/record")
    public ResponseEntity<String> recordWorkTimeControll(@RequestBody WorkTimeRequestDto request) {

        // 유효성 검사
        int errorCode = workTimeChecker.isValidWorkTime(request);
        if (errorCode != 0) {// 에러코드 0 = 정상
            return ResponseEntity
                    .badRequest()   
                    .body(errorHandler.handleError(errorCode));
        }

        // 서비스 호출
        workTimeService.recordWorkTime(
            request.getUserId(),
            request.getWorkDate(),
            request.getStartTime(),
            request.getEndTime()
        );

        // 시간 포맷 (HH:mm 형식) -> localDateTime 전체 출력하면 너무 길어서 보기 불편
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        String startTime = request.getStartTime().format(timeFormatter);
        String endTime = request.getEndTime().format(timeFormatter);

        return ResponseEntity.ok("근무시간 기록 완료! 규히야 민성아 수고했어!!" + "\r\n"+"사용자ID: " + request.getUserId()+ "\r\n" + "날짜: " + request.getWorkDate()
        + "\r\n"+ "출근시간: " + startTime + "\r\n" + "퇴근시간: " + endTime); 
    }

    //근무시간 조회 (QueryPara(Input) -> Json(Retunn))
    @GetMapping("/{userId}")
    public List<WorkTimeRequestDto> getWorkTimeControll (
            @PathVariable ("userId") Long userId,
            @RequestParam("workDate") LocalDate workDate
    ) {
        List<WorkTimeRequestDto> results = workTimeService.getWorkTime(userId, workDate);
        return results;
    }
}
