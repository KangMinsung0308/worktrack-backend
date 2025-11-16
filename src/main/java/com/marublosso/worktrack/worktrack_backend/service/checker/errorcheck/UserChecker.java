package com.marublosso.worktrack.worktrack_backend.service.checker.errorcheck;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;

import com.marublosso.worktrack.worktrack_backend.dto.WorkTimeRequestDto;

@Component
public class UserChecker {

	private final JdbcTemplate jdbcTemplate;

	public UserChecker(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

    public int isValidUser(WorkTimeRequestDto request) {
        
        //에러 코드 초기화
        int errorCode = 0;

        Long userId = request.getUserId();

			// 1. SQL문 작성
			String sql =  
				" SELECT user_id " +
				" FROM worktrack_db.USERS " +
				" WHERE user_id = ? " 
			;
    	 
			// 2. JDBC 템플릿을 사용하여 데이터베이스에서 조회
		List<WorkTimeRequestDto> results = jdbcTemplate.query(
			sql,
			new RowMapper<WorkTimeRequestDto>(){
				@Override
				public WorkTimeRequestDto mapRow(ResultSet rs, int rowNum) throws SQLException {
					WorkTimeRequestDto dto = new WorkTimeRequestDto();
					dto.setUserId(rs.getLong("user_id"));
					return dto;
				}
			},userId);

			//3.유저가 없을 경우 에러코드 3 반환
			if(results.isEmpty()){
				errorCode = 3; // 유효하지 않은 사용자
				return errorCode;
			}

        // 유저가 있을경우
        return errorCode;
    }

}
