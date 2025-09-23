package com.marublosso.worktrack.worktrack_backend.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;



@Service
public class WorkTimeService {
	
    private final JdbcTemplate jdbcTemplate;


    public WorkTimeService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void recordWorkTime(
	    Long userId,               // 사용자 ID
	    LocalDate workDate,        // 근무 날짜
	    LocalDateTime startTime,   // 출근 시간
	    LocalDateTime endTime,     // 퇴근 시간
	    Double totalHours,         // 총 근무 시간
	    Double overtime            // 잔업/휴게 시간
    	) {
    
    	
    	String sql = "INSERT INTO ATTENDANCE " +
                "(user_id, work_date, start_time, end_time, total_hours, overtime, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	   // 현재 시간
	   Timestamp now = Timestamp.valueOf(LocalDateTime.now());
	
	   jdbcTemplate.update(
	       sql,	
	       userId,
	       workDate,
	       startTime,
	       endTime,
	       totalHours,
	       overtime,
	       now,
	       now
	   );
    }

    public Object getWorkTime(Long userId) {
        // DB 조회 로직 구현
        return null;
    }
}
