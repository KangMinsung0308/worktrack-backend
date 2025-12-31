package com.marublosso.worktrack.worktrack_backend.service.biz.java.features;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.marublosso.worktrack.worktrack_backend.dto.LoginUserDto;
import com.marublosso.worktrack.worktrack_backend.dto.PutWorktimeRequestDto;

import com.marublosso.worktrack.worktrack_backend.entity.AttendanceEntity;
import com.marublosso.worktrack.worktrack_backend.exception.InvalidWorkTimeException;
import com.marublosso.worktrack.worktrack_backend.service.biz.java.util.timetools.TimeCalculator;
import com.marublosso.worktrack.worktrack_backend.repository.repo.WorkTimeRepository;

@Service
public class PutWorkTimeService {

    private final TimeCalculator TimeCalculator;
    private final WorkTimeRepository workTimeRepository;

    // DI (스프링 빈으로 주입)
    public PutWorkTimeService(TimeCalculator TimeCalculator, WorkTimeRepository workTimeRepository) {
        this.TimeCalculator = TimeCalculator;
        this.workTimeRepository = workTimeRepository;
    }

    // Update Worktime ((PutWorktimeRequestDto)-> Entity -> DB(Update))
    public void UpdateWorkTime(PutWorktimeRequestDto request, LoginUserDto loginUser) {

        // 연차 or 대휴인 경우 유효성 검사 생략
        if (request.getWorkType() == 1 || request.getWorkType() == 3) { // 연차 or 대휴인 경우
            // DTO -> Entity 변환
            AttendanceEntity attendanceEntity = yasumiDtoToEntity(request, loginUser);

            // DB 업데이트
            workTimeRepository.upsertWorkTime(attendanceEntity);
            return;
        }else {
            // 근무시간 유효성 검사
            IsVaildWorkTime(request, loginUser);
        }   

        // 요청 DTO 값 로컬 변수로 저장
        LocalDateTime start = request.getStartTime();
        LocalDateTime end = request.getEndTime();
        Double yasumi = request.getYasumiTime();

        // 시간 계산
        double totalHours = TimeCalculator.calculateOnlyWorkTime(start, end, yasumi);
        double overtime = TimeCalculator.calculateOnlyOvertime(start, end, yasumi);

        // DTO -> Entity 변환
        AttendanceEntity attendanceEntity = normalDtoToEntity(request, loginUser, totalHours, overtime);

        // DB 업데이트
        workTimeRepository.upsertWorkTime(attendanceEntity);
    }

    private void IsVaildWorkTime(PutWorktimeRequestDto request, LoginUserDto loginUser) {

        // 유효성 검사
        // null 체크
        if (request.getStartTime() == null || request.getEndTime() == null) {
            throw new InvalidWorkTimeException("출근시간 또는 퇴근시간은 반드시 입력되어야 합니다.");
        }

        // 출근시간이 퇴근시간보다 이후인지 체크
        if (request.getEndTime().isBefore(request.getStartTime())
                || request.getEndTime().equals(request.getStartTime())) {
            throw new InvalidWorkTimeException("퇴근시간은 출근시간보다 이후여야 합니다.");
        }

        if (request.getWorkType() == 2) { // 반차인 경우
            // 처리 없음
        }
    }

    private AttendanceEntity yasumiDtoToEntity(PutWorktimeRequestDto request, LoginUserDto loginUser) {
        LocalDateTime defaultTime = request.getWorkDate().atStartOfDay(); // 00:00 기준
        AttendanceEntity attendanceEntity = AttendanceEntity.builder()
                .user_id(loginUser.getId())
                .work_date(request.getWorkDate())
                .start_time(defaultTime)
                .end_time(defaultTime)
                .work_type(request.getWorkType())
                .bikou(request.getBikou())
                .updated_at(LocalDateTime.now())// 서버 현재 시간
                .build();
        return attendanceEntity;
    }

    private AttendanceEntity normalDtoToEntity(PutWorktimeRequestDto request, LoginUserDto loginUser, double totalHours,
            double overtime) {

        // DTO -> Entity 변환
        AttendanceEntity attendanceEntity = AttendanceEntity.builder()
                .user_id(loginUser.getId())
                .work_date(request.getWorkDate())
                .start_time(request.getStartTime())
                .end_time(request.getEndTime())
                .work_type(request.getWorkType())
                .total_hours(totalHours)
                .overtime(overtime)
                .bikou(request.getBikou())
                .updated_at(LocalDateTime.now())// 서버 현재 시간
                .build();

        return attendanceEntity;
    }

}
