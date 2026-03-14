package com.marublosso.worktrack.worktrack_backend.service.biz.java.features;

import org.springframework.stereotype.Service;

import com.marublosso.worktrack.worktrack_backend.dto.LoginUserDto;
import com.marublosso.worktrack.worktrack_backend.dto.ProfileRequestDto;
import com.marublosso.worktrack.worktrack_backend.entity.user_profilesEntity;
import com.marublosso.worktrack.worktrack_backend.repository.repo.ProfileRepository;

@Service
public class UpdateProfileService {

    private final ProfileRepository ProfileRepository;
    public UpdateProfileService(ProfileRepository ProfileRepository) {
        this.ProfileRepository = ProfileRepository;
    }

    public void updateProfile(ProfileRequestDto profileRequestDto, LoginUserDto loginUser) {

        // 유효성 검사

        Long id = loginUser.getId(); // 로그인한 유저의 ID를 가져옴
        // dto-> entity 매핑
        user_profilesEntity entity = mapToEntity(profileRequestDto, id);

        // 레포지토리 호출후 변경
        ProfileRepository.updateProfile(entity);
    }

    private user_profilesEntity mapToEntity(ProfileRequestDto profileRequestDto, Long id){
        return user_profilesEntity.builder()
                .id(id)
                .name(profileRequestDto.getName())
                .dept(profileRequestDto.getDept())
                .build();
    }
}
