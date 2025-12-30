package com.marublosso.worktrack.worktrack_backend.service.biz.java.features;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.marublosso.worktrack.worktrack_backend.dto.LoginUserDto;
import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;
import com.marublosso.worktrack.worktrack_backend.repository.repo.WorkTimeRepository;
import com.marublosso.worktrack.worktrack_backend.service.biz.java.util.timetools.DaySelector;

@Service
public class GetMonthWorkTimeService {
    private final WorkTimeRepository workTimeRepository;

	private final DaySelector daySelector;


    public GetMonthWorkTimeService(DaySelector daySelector, WorkTimeRepository workTimeRepository) {
        this.workTimeRepository = workTimeRepository;
		this.daySelector = daySelector;

    }

    // 근무시간 조회 ((Input) -> Json(Return))
    public List<WorkTimeRequestDto> getWorkTime(
	    LocalDate workDate,			// 근무 날짜    
        LoginUserDto loginUserDto   // 로그인한 사용자 정보
    )   {

        LocalDate firstDay = daySelector.Firstday(workDate);
		LocalDate lastDay = daySelector.Lastday(workDate);
        
        return workTimeRepository.findWorkTimeByUserAndDateRange(loginUserDto.getId(), firstDay, lastDay);
    }
}
