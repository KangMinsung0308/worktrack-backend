package com.marublosso.worktrack.worktrack_backend.controller;

import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;
import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeResponseDto;
import com.marublosso.worktrack.worktrack_backend.service.WorkTimeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/worktime")
public class WorkTimeController {

    private final WorkTimeService workTimeService;

    public WorkTimeController(WorkTimeService workTimeService) {
        this.workTimeService = workTimeService;
    }

    @PostMapping("/record")
    public String recordWorkTime(@RequestBody WorkTimeRequestDto request) {
        // 서비스 호출
    	workTimeService.recordWorkTime(
            request.getUserId(),
            request.getWorkDate(),
            request.getStartTime(),
            request.getEndTime(),
            request.getTotalHours(),
            request.getOvertime()
    	);
        return "근무시간 기록 완료"; 
    }

    @GetMapping("/{userId}")
    public WorkTimeResponseDto getWorkTime(@PathVariable Long userId) {
        
        // 서비스 호출
        return new WorkTimeResponseDto();
    }
}
