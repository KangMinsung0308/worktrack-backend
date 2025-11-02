package com.marublosso.worktrack.worktrack_backend.controller;

import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;
import com.marublosso.worktrack.worktrack_backend.service.WorkTimeService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "*") // <-- 로컬 테스트용: 필요시 특정 origin으로 변경하세요 (지피티가 써준거 이게 먼지 모름 일단 서버 올리면 지울거임)
@RestController
@RequestMapping("/worktime")
public class WorkTimeController {

    private final WorkTimeService workTimeService;

    public WorkTimeController(WorkTimeService workTimeService) {
        this.workTimeService = workTimeService;
    }
    
    //근무시간 DB 기록 (JSON (Input) -> DB(Record))
    @PostMapping("/record")
    public ResponseEntity<String> recordWorkTimeControll(@RequestBody WorkTimeRequestDto request) {
        // 서비스 호출
        workTimeService.recordWorkTime(
            request.getUserId(),
            request.getWorkDate(),
            request.getStartTime(),
            request.getEndTime()
        );
        return ResponseEntity.ok("근무시간 기록 완료"); 
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
