package com.marublosso.worktrack.worktrack_backend.service.biz.java.features;

import com.marublosso.worktrack.worktrack_backend.dto.LoginUserDto;
import com.marublosso.worktrack.worktrack_backend.entity.User;
import org.springframework.stereotype.Service;

import com.marublosso.worktrack.worktrack_backend.repository.LoginRepository;
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
        User user = User.builder()
                .username(userRequestDto.getUsername())
                .password_hash(userRequestDto.getPassword())
                .build();

        // 유저 정보 조회
        User foundUser = loginRepository.SearchUserInfo(user);

        // 로그인 실패
        if (foundUser == null) {
            return null;
        }
        // 로그인 성공
        return LoginUserDto.from(loginRepository.SearchUserInfo(foundUser));

    }

}
