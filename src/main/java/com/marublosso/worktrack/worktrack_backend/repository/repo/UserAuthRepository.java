package com.marublosso.worktrack.worktrack_backend.repository.repo;


import com.marublosso.worktrack.worktrack_backend.entity.UserAuth;

public interface UserAuthRepository {

    // 로그인용 유저 정보 체크
    UserAuth searchUserInfo(UserAuth user);

    // 회원가입용 유저 이메일이 존재하는지 체크
    int searchAcountOnlyInfo(UserAuth user);

    // 회원가입용 유저 정보 등록
    Long insertNewUser(UserAuth user);

}
