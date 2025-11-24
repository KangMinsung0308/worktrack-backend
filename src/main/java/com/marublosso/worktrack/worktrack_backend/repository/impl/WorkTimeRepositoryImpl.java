package com.marublosso.worktrack.worktrack_backend.repository.impl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;
import com.marublosso.worktrack.worktrack_backend.repository.WorkTimeRepository;
import com.marublosso.worktrack.worktrack_backend.repository.mapper.workTimeMapper.WorkTimeRowMapper;

@Repository
public class WorkTimeRepositoryImpl implements WorkTimeRepository {

	private final JdbcTemplate jdbcTemplate;

	public WorkTimeRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void insertWorkTime(WorkTimeRequestDto dto) {

		// 매개변수(DTO)로 지역변수 초기화
		Long userId = dto.getUserId();
		LocalDate workDate = dto.getWorkDate();
		LocalDateTime startTime = dto.getStartTime();
		LocalDateTime endTime = dto.getEndTime();
		String bikou = dto.getBikou();
		Long worktype = dto.getWorkType();
		Double totalHours = dto.getTotalHours();
		Double overtime = dto.getOvertime();

		// 1. SQL문 작성
		String sql = "INSERT INTO ATTENDANCE " +
				"(user_id, work_date, start_time, end_time, total_hours, overtime, created_at, updated_at, bikou, work_type ) "
				+
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ? , ? ) ";

		// 현재 시간
		Timestamp now = Timestamp.valueOf(LocalDateTime.now());

		// 2. JDBC 템플릿을 사용하여 데이터베이스에 기록ß
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
				bikou,
				worktype);
	}

	@Override
	public List<WorkTimeRequestDto> findWorkTimeByUserAndDateRange(Long userId, LocalDate firstDay, LocalDate lastDay) {
		// 1. SQL문 작성
		String sql = " SELECT a.* " +
				" FROM worktrack_db.ATTENDANCE a " +
				" JOIN (" +
				"	SELECT user_id, work_date, MAX(attendance_id) AS latest " +
				"	FROM worktrack_db.ATTENDANCE " +
				"	Where user_id = ? and work_date Between ? and ? " +
				"	GROUP BY user_id, work_date " +
				" ) b ON a.user_id = b.user_id " +
				" AND a.work_date = b.work_date " +
				" AND a.attendance_id = b.latest " +
				" ORDER By work_date ";

		return jdbcTemplate.query(sql, new WorkTimeRowMapper(), userId, firstDay, lastDay);
	}
}