package com.marublosso.worktrack.worktrack_backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class WorkTimeRequestDto {
    private Long userId;
    private LocalDate workDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double totalHours;
    private Double overtime;
    private Double yasumiTime;
    private Long workType;
    private String bikou;
    private boolean isHalfDay;
}
