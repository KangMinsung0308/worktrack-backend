package com.marublosso.worktrack.worktrack_backend.service.biz.calculation;
import java.time.LocalDateTime;

import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;

import java.time.Duration;


public class GeneralOverTime implements TimeCalculator {

    private static final double STANDARD_HOURS = 9.0;
    private static final double LUNCH_TIME = 1.0;
    private static final double STOLEN_TIME = 0.75; // jpxi 강제 휴계시간

    @Override
    public WorkTimeRequestDto calculateOvertime(Long worktype,LocalDateTime startTime, LocalDateTime endTime) {
        double totalHours = Duration.between(startTime, endTime).toMinutes() / 60.0;
        
        double overtime = 0.0;
        if (worktype == 1) { // JPXI 현장
            if (totalHours >= STANDARD_HOURS) {
                overtime = totalHours - STANDARD_HOURS;
                totalHours -= STOLEN_TIME; // 강제 휴게시간 차감
            }else if (totalHours < STANDARD_HOURS-0.75){
                // 강제 휴계시간 차감 없음
                // 잔업시간 없음
            }
        } else if (worktype == 2) { // APPLE 현장
            // 추가 예정 (최종적으로는 워크타입을 xml정의로 관리해서 유저가 직접 추가할수있도록 할듯?)
        }

        WorkTimeRequestDto dto = new WorkTimeRequestDto();
        dto.setStartTime(startTime);
        dto.setEndTime(endTime);
        dto.setTotalHours(totalHours-LUNCH_TIME);
        dto.setOvertime(overtime);
        return dto;
    }
}
