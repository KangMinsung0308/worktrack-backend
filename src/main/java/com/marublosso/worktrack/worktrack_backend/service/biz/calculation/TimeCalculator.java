package com.marublosso.worktrack.worktrack_backend.service.biz.calculation;

import java.time.LocalDateTime;

import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;

public interface TimeCalculator {
    WorkTimeRequestDto calculateOvertime(Long worktype,LocalDateTime startTime, LocalDateTime endTime);
}
