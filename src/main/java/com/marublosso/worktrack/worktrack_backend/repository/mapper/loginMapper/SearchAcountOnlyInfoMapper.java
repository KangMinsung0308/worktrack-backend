package com.marublosso.worktrack.worktrack_backend.repository.mapper.loginMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.marublosso.worktrack.worktrack_backend.entity.User;

    public class SearchAcountOnlyInfoMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .user_id(rs.getLong("user_id"))
                    .build();
        }
    }