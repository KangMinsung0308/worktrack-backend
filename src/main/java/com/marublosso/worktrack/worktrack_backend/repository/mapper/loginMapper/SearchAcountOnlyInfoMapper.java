package com.marublosso.worktrack.worktrack_backend.repository.mapper.loginMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.marublosso.worktrack.worktrack_backend.entity.user_authEntity;

    public class SearchAcountOnlyInfoMapper implements RowMapper<user_authEntity> {

        @Override
        public user_authEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return user_authEntity.builder()
                    .id(rs.getLong("id"))
                    .build();
        }
    }