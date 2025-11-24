package com.marublosso.worktrack.worktrack_backend.service.biz.java.errorcheck;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;

@Component
public class WorkTimeChecker {

    public int isValidWorkTime(WorkTimeRequestDto request) {
        
        int errorCode = 0;
        LocalDateTime startTime = request.getStartTime();
        LocalDateTime endTime = request.getEndTime();

        // null 체크 
        if (startTime == null || endTime == null) {
            errorCode = 1;
            return errorCode;
        }

        // 출근시간이 퇴근시간보다 이후인지 체크
        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            errorCode = 2;
            return errorCode;
        }

        // 모든 검사 통과 (정상)
        return 0;
    }

}
