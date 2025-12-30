package com.marublosso.worktrack.worktrack_backend.repository.repo;


import com.marublosso.worktrack.worktrack_backend.entity.User;

public interface LoginRepository {

    // 로그인용 유저 정보 체크
    User SearchUserInfo(User user);

    // 회원가입용 유저 이메일이 존재하는지 체크
    int SearchAcountOnlyInfo(User user);

    // 회원가입용 유저 정보 등록
    void InsertNewUser(User user);

}
