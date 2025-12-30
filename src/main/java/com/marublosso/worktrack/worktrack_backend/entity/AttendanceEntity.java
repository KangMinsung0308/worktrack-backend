package com.marublosso.worktrack.worktrack_backend.entity;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AttendanceEntity {

    private Long attendance_id;
    private Long user_id;
    private LocalDate work_date;
    private LocalDateTime start_time;
    private LocalDateTime end_time;
    private Double total_hours;
    private Double overtime;
    private Long work_type;
    private String bikou;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

}
