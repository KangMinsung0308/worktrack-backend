package com.marublosso.worktrack.worktrack_backend.service.biz.java.util.timetools;

import java.time.LocalDateTime;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;

import java.time.Duration;

@Component
public class GeneralOverTime implements TimeCalculator {

    // 기준 근무시간 및 휴게시간 설정(TODO: DB에서 관리하도록 변경 예정)
    private static final double STANDARD_HOURS = 9.0;
    private static final double LUNCH_TIME = 1.0;
    private static final double STOLEN_TIME = 0.75; // jpxi 강제 휴계시간

    @Override
    // TODO 쓰레기 로직(들어오는 값은 유저ID 인데 여기서는 현장번호로 인식해서 돌아가는중(不具合)... 수정필요 그리고 dto 리턴하면서
    // 너무 복잡하게 짜여있음 로직 분리 필요(재설게）)
    public WorkTimeRequestDto calculateOvertime(Long genBa, LocalDateTime startTime, LocalDateTime endTime) {
        double totalHours = Duration.between(startTime, endTime).toMinutes() / 60.0;

        double overtime = 0.0;
        if (genBa == 1) { // JPXI 현장
            if (totalHours >= STANDARD_HOURS) {
                overtime = totalHours - STANDARD_HOURS;
                totalHours -= STOLEN_TIME; // 강제 휴게시간 차감
            } else if (totalHours < STANDARD_HOURS - 0.75) {
                // 강제 휴계시간 차감 없음
                // 잔업시간 없음
            }
        } else if (genBa == 2) { // APPLE 현장
            // 추가 예정 (최종적으로는 워크타입을 xml정의로 관리해서 유저가 직접 추가할수있도록 할듯?)
        }

        WorkTimeRequestDto dto = new WorkTimeRequestDto();
        dto.setStartTime(startTime);
        dto.setEndTime(endTime);
        dto.setTotalHours(totalHours - LUNCH_TIME);
        dto.setOvertime(overtime);
        return dto;
    }

    /******************************************************************
     * 총 근무시간 계산 유틸리티
     *
     * @param startTime  업무 시작시간
     * @param endTime    업무 종료시간
     * @param YasumiTime 휴식시간 ( 근무 시간제외분 )
     * 
     * @return 총 근무시간 (Double)
     * 
     */
    @Override
    public Double calculateOnlyWorkTime(LocalDateTime startTime, LocalDateTime endTime, Double YasumiTime) {
        if (startTime == null || endTime == null) {
            return 0.0;
        }

        // 총 근무 시간 계산 (분 단위 → 시간)
        double totalHours = Duration.between(startTime, endTime).toMinutes() / 60.0;

        // 휴가시간(YasumiTime) 제외
        if (YasumiTime != null) {
            totalHours -= YasumiTime;
        }

        // 음수 방지 (음수 -> 0.0 반환)
        return Math.max(totalHours, 0.0);
    }

    /******************************************************************
     * 총 잔업시간 계산 유틸리티
     *
     * @param startTime  업무 시작시간
     * @param endTime    업무 종료시간
     * @param YasumiTime 휴식시간 ( 근무 시간제외분 )
     * 
     * @return 잔업시간 (Double)
     * 
     */
    @Override
    public Double calculateOnlyOvertime(LocalDateTime startTime, LocalDateTime endTime, Double YasumiTime) {
        double totalHours = calculateTotalHours(startTime, endTime, YasumiTime);

        // 음수 방지 (음수 -> 0.0 반환)
        return Math.max(totalHours - STANDARD_HOURS, 0.0);
    }

    /******************************************************************
     * 공통 메소드
     * 
     * 업무 시작시간, 종료시간, 휴계시간을 기반으로 총 근무시간을 계산한다
     *
     * @param startTime  업무 시작시간
     * @param endTime    업무 종료시간
     * @param YasumiTime 휴식시간 ( 근무 시간제외분 )
     * 
     * @return 총 근무시간 (Double)
     * 
     */
    private double calculateTotalHours(LocalDateTime startTime, LocalDateTime endTime, Double YasumiTime) {
        if (startTime == null || endTime == null)
            return 0.0;

        double totalHours = Duration.between(startTime, endTime).toMinutes() / 60.0;
        double yasumi = YasumiTime != null ? YasumiTime : 0.0;
        totalHours -= yasumi;

        return Math.max(totalHours, 0.0);
    }
}
