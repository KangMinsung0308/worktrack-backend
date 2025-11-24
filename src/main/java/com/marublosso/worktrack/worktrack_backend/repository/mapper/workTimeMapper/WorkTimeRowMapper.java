package com.marublosso.worktrack.worktrack_backend.repository.mapper.workTimeMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;

// RowMapper: ResultSet을 DTO로 변환
public class WorkTimeRowMapper implements RowMapper<WorkTimeRequestDto> {
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
}