package com.marublosso.worktrack.worktrack_backend.service.biz.java.features;

import com.marublosso.worktrack.worktrack_backend.dto.LoginUserDto;
import com.marublosso.worktrack.worktrack_backend.entity.user_authEntity;
import com.marublosso.worktrack.worktrack_backend.repository.repo.LoginRepository;

import org.springframework.stereotype.Service;

import com.marublosso.worktrack.worktrack_backend.dto.LoginRequestDto;



@Service
public class LoginService {

    private final LoginRepository loginRepository;
    

    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }
    
    public LoginUserDto login(LoginRequestDto userRequestDto) {

        // TODO : 유효성 검사 

        // DTO -> Entity 변환
        user_authEntity user = user_authEntity.builder()
                .email(userRequestDto.getUsername())
                .password_hash(userRequestDto.getPassword())
                .build();

        // 유저 정보 조회
        user_authEntity foundUser = loginRepository.SearchUserInfo(user);

        // 로그인 실패
        if (foundUser == null) {
            return null;
        }
        // 로그인 성공
        return LoginUserDto.from(loginRepository.SearchUserInfo(foundUser));

    }

}
