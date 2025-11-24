package com.marublosso.worktrack.worktrack_backend.service.biz.java.features;

import org.springframework.stereotype.Service;

import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;
import com.marublosso.worktrack.worktrack_backend.service.biz.java.util.timetools.GeneralOverTime;
import com.marublosso.worktrack.worktrack_backend.repository.WorkTimeRepository;

@Service
public class PostWorkTimeService {
	
	private final GeneralOverTime generalOverTime;
	private final WorkTimeRepository workTimeRepository;

    public PostWorkTimeService(GeneralOverTime generalOverTime, WorkTimeRepository workTimeRepository){ 
		this.generalOverTime = generalOverTime;
		this.workTimeRepository = workTimeRepository;
    }
	// 근무시간 DB 기록 ((Input) -> DB(Record))
    public void recordWorkTime(WorkTimeRequestDto request) {

		// 1. 잔업시간, 총근무 시간 계산
        WorkTimeRequestDto result = generalOverTime.calculateOvertime(
            request.getUserId(),
            request.getStartTime(),
            request.getEndTime()
        );

		// 2. DTO 생성 및 값 설정
		WorkTimeRequestDto workTimeToSave = new WorkTimeRequestDto();
        workTimeToSave.setUserId(request.getUserId());
        workTimeToSave.setWorkDate(request.getWorkDate());
        workTimeToSave.setStartTime(request.getStartTime());
        workTimeToSave.setEndTime(request.getEndTime());
        workTimeToSave.setTotalHours(result.getTotalHours());
        workTimeToSave.setOvertime(result.getOvertime());
        workTimeToSave.setBikou(request.getBikou());
		workTimeToSave.setWorkType(request.getWorkType());

		// 3. DB 기록
		workTimeRepository.insertWorkTime(workTimeToSave);

    }
}
