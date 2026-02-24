package com.marublosso.worktrack.worktrack_backend.repository.repo;


import com.marublosso.worktrack.worktrack_backend.entity.auth_profilesJoinEntity;
import com.marublosso.worktrack.worktrack_backend.entity.user_authEntity;

public interface LoginRepository {

    // 로그인용 유저 정보 체크
    auth_profilesJoinEntity SearchUserInfo(user_authEntity user);

    // 회원가입용 유저 이메일이 존재하는지 체크
    int SearchAcountOnlyInfo(user_authEntity user);

    // 회원가입용 유저 정보 등록
    void InsertNewUser(user_authEntity user);

}
