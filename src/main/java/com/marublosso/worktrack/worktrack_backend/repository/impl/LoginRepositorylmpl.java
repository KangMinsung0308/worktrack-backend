package com.marublosso.worktrack.worktrack_backend.repository.impl;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.marublosso.worktrack.worktrack_backend.entity.auth_profilesJoinEntity;
import com.marublosso.worktrack.worktrack_backend.entity.user_authEntity;
import com.marublosso.worktrack.worktrack_backend.entity.user_profilesEntity;
import com.marublosso.worktrack.worktrack_backend.repository.mapper.loginMapper.UserRowMapper;
import com.marublosso.worktrack.worktrack_backend.repository.repo.LoginRepository;

@Repository
public class LoginRepositorylmpl implements LoginRepository {

    private final JdbcTemplate jdbcTemplate;

    public LoginRepositorylmpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public auth_profilesJoinEntity SearchUserInfo(user_authEntity user) {

        String sql = " SELECT ua.id, " +
                " ua.email, " +
                " up.name, " +
                " up.dept" +
                " FROM worktrack_db.user_auth ua" +
                " INNER JOIN worktrack_db.user_profiles up" +
                "     ON ua.id = up.id" +
                " WHERE ua.email = ?" +
                " AND ua.password_hash = ?";
        Object[] params = new Object[] { user.getEmail(), user.getPassword_hash() };

        try {
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), params);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int SearchAcountOnlyInfo(user_authEntity user) {
        String sql = " SELECT Count(email) " +
                " from worktrack_db.user_auth " +
                " Where email = ? ";
        Object[] params = new Object[] { user.getEmail() };

        Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                params);

        // 삼항 연산자 (조건식 ? 참일때값 : 거짓일때값)
        return count != null ? count : 999;
    }

    @Override
    public void InsertNewUser(user_authEntity user) {
        // 1. SQL문 작성
        String sql = "INSERT INTO worktrack_db.user_auth " +
                " (email , password_hash) " +
                "VALUES (?, ?) ";

        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getPassword_hash());

    }
}