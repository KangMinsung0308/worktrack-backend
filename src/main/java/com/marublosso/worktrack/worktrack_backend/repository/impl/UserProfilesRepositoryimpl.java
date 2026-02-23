package com.marublosso.worktrack.worktrack_backend.repository.impl;

import com.marublosso.worktrack.worktrack_backend.repository.repo.UserProfilesRepository;
import com.marublosso.worktrack.worktrack_backend.entity.UserProfiles;

import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository
public class UserProfilesRepositoryimpl implements UserProfilesRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserProfilesRepositoryimpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertUserProfile(UserProfiles userProfiles) {

        // 1. SQL문 작성
        String sql = "INSERT INTO user_profiles " +
                " (id, name, dept, role) " +
                " VALUES (?, ?, ?, ?) ";

        jdbcTemplate.update(sql,
                userProfiles.getId(),
                userProfiles.getName(),
                userProfiles.getDept(),
                userProfiles.getRole());
    }

    @Override
    public void updateUserProfile(UserProfiles userProfiles) {

        // 1. SQL문 작성
        String sql = "UPDATE user_profiles " +
                " 	SET name = ? , " +
                "   	dept = ?   " +
                " WHERE id = ? ";

        jdbcTemplate.update(sql,
                userProfiles.getName(),
                userProfiles.getDept(),
                userProfiles.getId());
    };

    @Override
    public UserProfiles selectUserProfileById(Long id) {

        String sql = " SELECT name, dept, role " +
                " from user_profiles " +
                " Where id = ? ";
        Object[] params = new Object[] { id };

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            UserProfiles userProfiles = new UserProfiles();
            userProfiles.setName(rs.getString("name"));
            userProfiles.setDept(rs.getString("dept"));
            userProfiles.setRole(rs.getString("role"));
            return userProfiles;
        }, params);
    }
}
