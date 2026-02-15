package com.marublosso.worktrack.worktrack_backend.service.biz.java.features;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

import com.marublosso.worktrack.worktrack_backend.dto.PutWorktimeRequestDto;

import com.marublosso.worktrack.worktrack_backend.entity.Attendance;
import com.marublosso.worktrack.worktrack_backend.exception.InvalidWorkTimeException;
import com.marublosso.worktrack.worktrack_backend.service.biz.java.util.timetools.TimeCalculator;
import com.marublosso.worktrack.worktrack_backend.repository.repo.AttendanceRepository;

@Service
public class PutWorkTimeService {

    private final TimeCalculator TimeCalculator;
    private final AttendanceRepository workTimeRepository;

    // DI (스프링 빈으로 주입)
    public PutWorkTimeService(TimeCalculator TimeCalculator, AttendanceRepository workTimeRepository) {
        this.TimeCalculator = TimeCalculator;
        this.workTimeRepository = workTimeRepository;
    }

    // Update Worktime ((PutWorktimeRequestDto)-> Entity -> DB(Update))
    public void UpdateWorkTime(PutWorktimeRequestDto request, Long id) {

        // 연차 or 대휴인 경우 유효성 검사 생략
        if (request.getWorkType() == 1 || request.getWorkType() == 3) { // 연차 or 대휴인 경우
            // DTO -> Entity 변환
            Attendance attendanceEntity = yasumiDtoToEntity(request, id);

            // DB 업데이트
            workTimeRepository.upsertWorkTime(attendanceEntity);
            return;
        }else {
            // 근무시간 유효성 검사
            IsVaildWorkTime(request);
        }   

        // 요청 DTO 값 로컬 변수로 저장
        LocalDateTime start = request.getStartTime();
        LocalDateTime end = request.getEndTime();
        if (request.getYasumiTime() == null) {
            // TODO: 기본 휴게시간 1시간(60분)로 설정, 추후 설정 가능하도록 변경 예정(유저 세팅 테이블의 있는 테이블의 값으로 변경)
            request.setYasumiTime(60.0);
        }
        Double yasumi = request.getYasumiTime();


        // 시간 계산
        double totalHours = TimeCalculator.calculateOnlyWorkTime(start, end, yasumi);
        double overtime = TimeCalculator.calculateOnlyOvertime(start, end, yasumi);

        // DTO -> Entity 변환
        Attendance attendanceEntity = normalDtoToEntity(request, id, totalHours, overtime, yasumi);

        // DB 업데이트
        workTimeRepository.upsertWorkTime(attendanceEntity);
    }

    private void IsVaildWorkTime(PutWorktimeRequestDto request) {

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

    private Attendance yasumiDtoToEntity(PutWorktimeRequestDto request, Long id) {
        LocalDateTime defaultTime = request.getWorkDate().atStartOfDay(); // 00:00 기준
        Attendance attendanceEntity = Attendance.builder()
                .user_id(id)
                .work_date(request.getWorkDate())
                .start_time(defaultTime)
                .end_time(defaultTime)
                .work_type(request.getWorkType())
                .yasumi_time(0.0)
                .bikou(request.getBikou())
                .updated_at(LocalDateTime.now())// 서버 현재 시간
                .build();

        return attendanceEntity;
    }

    private Attendance normalDtoToEntity(PutWorktimeRequestDto request, Long id, double totalHours,
            double overtime, double yasumi) {

        // DTO -> Entity 변환
        Attendance attendanceEntity = Attendance.builder()
                .user_id(id)
                .work_date(request.getWorkDate())
                .start_time(request.getStartTime())
                .end_time(request.getEndTime())
                .work_type(request.getWorkType())
                .total_hours(totalHours)
                .overtime(overtime)
                .yasumi_time(yasumi)
                .bikou(request.getBikou())
                .updated_at(LocalDateTime.now())// 서버 현재 시간
                .build();

        return attendanceEntity;
    }

}
