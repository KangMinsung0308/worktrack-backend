package com.marublosso.worktrack.worktrack_backend.repository.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.marublosso.worktrack.worktrack_backend.entity.User;
import com.marublosso.worktrack.worktrack_backend.repository.mapper.loginMapper.SearchAcountOnlyInfoMapper;
import com.marublosso.worktrack.worktrack_backend.repository.mapper.loginMapper.UserRowMapper;
import com.marublosso.worktrack.worktrack_backend.repository.repo.LoginRepository;

@Repository
public class LoginRepositorylmpl implements LoginRepository {

    private final JdbcTemplate jdbcTemplate;

    public LoginRepositorylmpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User SearchUserInfo(User user) {

        String sql = " SELECT user_id , username , password_hash, name, dept, role " +
                " from worktrack_db.USERS " +
                " Where username = ? and password_hash  = ? ";
        Object[] params = new Object[] { user.getUsername(), user.getPassword_hash() };

        try {
            return jdbcTemplate.queryForObject(sql, new UserRowMapper(), params);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int SearchAcountOnlyInfo(User user) {
        String sql = " SELECT Count(username) " +
                " from worktrack_db.USERS " +
                " Where username = ? ";
        Object[] params = new Object[] { user.getUsername() };

        Integer count = jdbcTemplate.queryForObject(
                sql,
                Integer.class,
                params);

        // 삼항 연산자 (조건식 ? 참일때값 : 거짓일때값)
        return count != null ? count : 999;
    }

    @Override
    public void InsertNewUser(User user) {
        // 1. SQL문 작성
        String sql = "INSERT INTO worktrack_db.USERS " +
                " (username , password_hash, name, dept, role) " +
                "VALUES (?, ?, ?, ?, ?) ";

        jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPassword_hash(),
                user.getName(),
                user.getDept(),
                user.getRole());

    }

}
