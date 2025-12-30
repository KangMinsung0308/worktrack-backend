package com.marublosso.worktrack.worktrack_backend.repository.repo;

import java.time.LocalDate;
import java.util.List;

import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;
import com.marublosso.worktrack.worktrack_backend.entity.AttendanceEntity;

public interface WorkTimeRepository {
    // CREATE
    void insertWorkTime(WorkTimeRequestDto workTimeRequestDto);

    // READ
    List<WorkTimeRequestDto> findWorkTimeByUserAndDateRange(Long userId, LocalDate firstDay, LocalDate lastDay);

    // UPDATE
    void updateWorkTime(AttendanceEntity attendance);

    // UPSERT (성공하면 create. update문 삭제하고 대채하기)
    void upsertWorkTime(AttendanceEntity attendanceEntity);

}