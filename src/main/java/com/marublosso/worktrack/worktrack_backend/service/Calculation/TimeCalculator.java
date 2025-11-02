package com.marublosso.worktrack.worktrack_backend.service.Calculation;

import java.time.LocalDateTime;

import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;

public interface TimeCalculator {
    WorkTimeRequestDto calculateOvertime(int worktype,LocalDateTime startTime, LocalDateTime endTime);
}
