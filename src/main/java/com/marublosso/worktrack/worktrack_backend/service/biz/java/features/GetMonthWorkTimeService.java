package com.marublosso.worktrack.worktrack_backend.service.biz.java.features;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;
import com.marublosso.worktrack.worktrack_backend.repository.repo.AttendanceRepository;
import com.marublosso.worktrack.worktrack_backend.service.biz.java.util.timetools.DaySelector;

@Service
public class GetMonthWorkTimeService {
    private final AttendanceRepository attendanceRepository;

	private final DaySelector daySelector;


    public GetMonthWorkTimeService(DaySelector daySelector, AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
		this.daySelector = daySelector;

    }

    // 근무시간 조회 ((Input) -> Json(Return))
    public List<WorkTimeRequestDto> getWorkTime(
	    LocalDate workDate,			// 근무 날짜    
        Long id   // 로그인한 사용자 정보
    )   {

        LocalDate firstDay = daySelector.Firstday(workDate);
		LocalDate lastDay = daySelector.Lastday(workDate);
        
        return attendanceRepository.findWorkTimeByUserAndDateRange(id, firstDay, lastDay);
    }
}
