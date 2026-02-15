package com.marublosso.worktrack.worktrack_backend.repository.impl;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.marublosso.worktrack.worktrack_backend.entity.UserAuth;
import com.marublosso.worktrack.worktrack_backend.repository.mapper.loginMapper.ReturnIDMapper;
import com.marublosso.worktrack.worktrack_backend.repository.repo.UserAuthRepository;

@Repository
public class UserAuthRepositorylmpl implements UserAuthRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserAuthRepositorylmpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserAuth searchUserInfo(UserAuth user) {

        String sql = " SELECT id  " +
                " from worktrack_db.user_auth " +
                " Where email = ? and password_hash  = ? ";
        Object[] params = new Object[] { user.getEmail(), user.getPasswordHash() };

        try {
            return jdbcTemplate.queryForObject(sql, new ReturnIDMapper(), params);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int searchAcountOnlyInfo(UserAuth user) {
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
    public Long insertNewUser(UserAuth user) {

        String sql = "INSERT INTO worktrack_db.user_auth " +
                " (email, password_hash) " +
                "VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        // INSERT 실행 및 생성된 사용자 ID(PK) 획득
        int rows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPasswordHash());
            return ps;
        }, keyHolder); // 업데이트된 행 수 반환

        if (rows != 1) {
            // 예상과 다른 행 수가 업데이트된 경우 예외 처리
            throw new IllegalStateException("회원가입 처리 중 사용자 계정 정보 저장에 실패했습니다. (처리 건수: " + rows + ")");
        }

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("회원가입 처리 중 생성된 사용자 ID를 가져오지 못했습니다.");
        }

        return key.longValue();
    }

}
