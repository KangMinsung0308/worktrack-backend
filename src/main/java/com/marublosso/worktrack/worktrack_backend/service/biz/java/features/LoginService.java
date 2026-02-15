package com.marublosso.worktrack.worktrack_backend.service.biz.java.features;

import com.marublosso.worktrack.worktrack_backend.entity.UserAuth;
import com.marublosso.worktrack.worktrack_backend.entity.UserProfiles;
import com.marublosso.worktrack.worktrack_backend.repository.repo.UserAuthRepository;
import com.marublosso.worktrack.worktrack_backend.repository.repo.UserProfilesRepository;

import org.springframework.stereotype.Service;

import com.marublosso.worktrack.worktrack_backend.dto.LoginRequestDto;
import com.marublosso.worktrack.worktrack_backend.dto.SessionUserProfile;



@Service
public class LoginService {

    private final UserAuthRepository userAuthRepository;
    private final UserProfilesRepository userProfilesRepository;

    public LoginService(UserAuthRepository userAuthRepository, UserProfilesRepository userProfilesRepository) {
        this.userAuthRepository = userAuthRepository;
        this.userProfilesRepository = userProfilesRepository;
    }
    
    public SessionUserProfile login(LoginRequestDto userRequestDto) {

        // TODO : 유효성 검사 

        // DTO -> Entity 변환
        UserAuth user = UserAuth.builder()
                .email(userRequestDto.getUsername())
                .passwordHash(userRequestDto.getPassword())
                .build();

        // 유저 정보 조회
        UserAuth foundUser = userAuthRepository.searchUserInfo(user);

        // 로그인 실패
        if (foundUser == null) {
            return null;
        }
        SessionUserProfile profilesUserDto = new SessionUserProfile();
        profilesUserDto.setId(foundUser.getId());

        
        UserProfiles userProfile = userProfilesRepository.selectUserProfileById(foundUser.getId());
        profilesUserDto.setName(userProfile.getName());
        profilesUserDto.setDept(userProfile.getDept());


        // 로그인 성공
        return profilesUserDto;

    }

}
