package com.marublosso.worktrack.worktrack_backend.service.biz.java.features;

import com.marublosso.worktrack.worktrack_backend.dto.LoginUserDto;
import com.marublosso.worktrack.worktrack_backend.entity.User;
import org.springframework.stereotype.Service;

import com.marublosso.worktrack.worktrack_backend.repository.LoginRepository;



@Service
public class LoginService {

    private final LoginRepository loginRepository;
    

    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }
    
    public User login(LoginUserDto userDto) {

        // TODO : 유효성 검사 
        User user = User.builder()
                .username(userDto.getUsername())
                .password_hash(userDto.getPassword())
                .build();


        // 로그인 로직 구현
        return loginRepository.SearchUserInfo(user);

    }

}
