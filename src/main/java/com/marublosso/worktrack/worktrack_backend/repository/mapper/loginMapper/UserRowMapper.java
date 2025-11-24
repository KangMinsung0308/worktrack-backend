package com.marublosso.worktrack.worktrack_backend.repository.mapper.loginMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.marublosso.worktrack.worktrack_backend.entity.User;

    public class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .user_id(rs.getLong("user_id"))
                    .username(rs.getString("username"))
                    .password_hash(rs.getString("password_hash"))
                    .name(rs.getString("name"))
                    .dept(rs.getString("dept"))
                    .role(rs.getString("role"))
                    .build();
        }
    }