package com.marublosso.worktrack.worktrack_backend.repository.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.marublosso.worktrack.worktrack_backend.entity.User;
import com.marublosso.worktrack.worktrack_backend.repository.LoginRepository;
import com.marublosso.worktrack.worktrack_backend.repository.mapper.loginMapper.UserRowMapper;

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

}
