package com.marublosso.worktrack.worktrack_backend.service.biz.java.util.timetools;

import java.time.LocalDateTime;

import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;

public interface TimeCalculator {
    // TODO 삭제
    WorkTimeRequestDto calculateOvertime(Long worktype, LocalDateTime startTime, LocalDateTime endTime);

    Double calculateOnlyWorkTime(LocalDateTime startTime, LocalDateTime endTime, Double YasumiTime);
    Double calculateOnlyOvertime(LocalDateTime startTime, LocalDateTime endTime, Double YasumiTime);
}
