package com.marublosso.worktrack.worktrack_backend.service.biz;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;
import com.marublosso.worktrack.worktrack_backend.service.biz.calculation.GeneralOverTime;
import com.marublosso.worktrack.worktrack_backend.util.timetools.DaySelector;

@Service
public class WorkTimeService {
	
    private final JdbcTemplate jdbcTemplate;
	private final GeneralOverTime generalOverTime;
	private final DaySelector daySelector;


    public WorkTimeService(JdbcTemplate jdbcTemplate, GeneralOverTime generalOverTime, DaySelector daySelector) {
        this.jdbcTemplate = jdbcTemplate;
		this.generalOverTime = generalOverTime;
		this.daySelector = daySelector;
    }
	// 근무시간 DB 기록 ((Input) -> DB(Record))
    public void recordWorkTime(WorkTimeRequestDto request) {

		// DTO객체를 사용해 지역변수 초기화
		Long userId = request.getUserId();
		LocalDate workDate = request.getWorkDate();
		LocalDateTime startTime = request.getStartTime();
		LocalDateTime endTime = request.getEndTime();
		String bikou = request.getBikou();

		// 1. 잔업시간, 총근무 시간 계산 (calculateOvertime 부품 사용)
        WorkTimeRequestDto result = generalOverTime.calculateOvertime(userId, startTime, endTime);
        Double totalHours = result.getTotalHours();
        Double overtime = result.getOvertime();
    	
		// 2. SQL문 작성
    	String sql = "INSERT INTO ATTENDANCE " +
                "(user_id, work_date, start_time, end_time, total_hours, overtime, created_at, updated_at, bikou) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? )";

	   // 현재 시간
	   Timestamp now = Timestamp.valueOf(LocalDateTime.now());

	   // 3. JDBC 템플릿을 사용하여 데이터베이스에 기록
	   jdbcTemplate.update(
	       sql,	
	       userId,
	       workDate,
	       startTime,
	       endTime,
	       totalHours,
	       overtime,
	       now,
	       now,
		   bikou
	   );
    }

	// 근무시간 조회 ((Input) -> Json(Return))
    public List<WorkTimeRequestDto> getWorkTime(
        // DB 조회 로직 구현
		Long userId,               	// 사용자 ID
	    LocalDate workDate			// 근무 날짜
		){
	
		LocalDate firstDay = daySelector.Firstday(workDate);
		LocalDate lastDay = daySelector.Lastday(workDate);

			// 1. SQL문 작성
			String sql =  
				" SELECT a.* " +
				" FROM worktrack_db.ATTENDANCE a " +
				" JOIN (" +
				"	SELECT user_id, work_date, MAX(attendance_id) AS latest " +
				"	FROM worktrack_db.ATTENDANCE " +
				"	Where user_id = ? and work_date Between ? and ? " +
				"	GROUP BY user_id, work_date " +
				" ) b ON a.user_id = b.user_id " +
				" AND a.work_date = b.work_date " +
				" AND a.attendance_id = b.latest " +
				" ORDER By work_date "
			;
    	 
			// 2. JDBC 템플릿을 사용하여 데이터베이스에서 조회
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
					dto.setBikou(rs.getString("bikou"));
					return dto;
				}
			},userId,firstDay, lastDay);
    
        return results;
    }
	
}
