package com.marublosso.worktrack.worktrack_backend.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import com.marublosso.worktrack.worktrack_backend.entity.user_profilesEntity;
import com.marublosso.worktrack.worktrack_backend.repository.repo.ProfileRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileRepositoryImpl implements ProfileRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProfileRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void updateProfile(user_profilesEntity profileEntity) {

        // 1. SQL문 작성
        String sql = "UPDATE worktrack_db.user_profiles SET name = ?, dept = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                profileEntity.getName(),
                profileEntity.getDept(),
                profileEntity.getId());
    }
}
