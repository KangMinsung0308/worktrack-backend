package com.marublosso.worktrack.worktrack_backend.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;


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

    public List<WorkTimeRequestDto> getWorkTime(
        // DB 조회 로직 구현
		Long userId,               	// 사용자 ID
	    LocalDate workDate			// 근무 날짜
		){

			String sql = 
			"SELECT " + 
				" * " +	 
			"FROM" + 
				" ATTENDANCE " +
			"WHERE" + 
				" user_Id = ? " +
			"AND" +
				" work_Date = ? "
			;
    	 
		List<WorkTimeRequestDto> results = jdbcTemplate.query(
			sql,
			new RowMapper<WorkTimeRequestDto>(){
				@Override
				public WorkTimeRequestDto mapRow(ResultSet rs, int rowNum) throws SQLException {
					WorkTimeRequestDto dto = new WorkTimeRequestDto();
					dto.setUserId(rs.getLong("user_id"));
					dto.setWorkDate(rs.getDate("work_date").toLocalDate());
					dto.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
					dto.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
					dto.setTotalHours(rs.getDouble("total_hours"));
					dto.setOvertime(rs.getDouble("overtime"));
					return dto;
				}
			},userId, workDate);
    
        return results;
    }
}
