package com.marublosso.worktrack.worktrack_backend.repository.mapper.loginMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import com.marublosso.worktrack.worktrack_backend.entity.auth_profilesJoinEntity;

    public class UserRowMapper implements RowMapper<auth_profilesJoinEntity> {

        @Override
        public auth_profilesJoinEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return auth_profilesJoinEntity.builder()
                    .id(rs.getLong("id"))
                    .email(rs.getString("email"))
                    .name(rs.getString("name"))
                    .dept(rs.getString("dept"))
                    .build();
        }
    }