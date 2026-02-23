package com.marublosso.worktrack.worktrack_backend.repository.mapper.loginMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.marublosso.worktrack.worktrack_backend.entity.UserAuth;

    public class ReturnIDMapper implements RowMapper<UserAuth> {

        @Override
        public UserAuth mapRow(ResultSet rs, int rowNum) throws SQLException {
            return UserAuth.builder()
                    .id(rs.getLong("id"))
                    .build();
        }
    }