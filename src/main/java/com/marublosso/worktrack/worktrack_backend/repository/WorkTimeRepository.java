package com.marublosso.worktrack.worktrack_backend.repository;

import java.time.LocalDate;
import java.util.List;

import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;

public interface WorkTimeRepository {
    // CREATE
    void insertWorkTime(WorkTimeRequestDto workTimeRequestDto);

    // READ
    List<WorkTimeRequestDto> findWorkTimeByUserAndDateRange(Long userId, LocalDate firstDay, LocalDate lastDay);

}