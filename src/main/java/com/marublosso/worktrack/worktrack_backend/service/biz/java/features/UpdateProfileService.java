package com.marublosso.worktrack.worktrack_backend.service.biz.java.features;

import org.springframework.stereotype.Service;

import com.marublosso.worktrack.worktrack_backend.dto.SessionUserProfile;
import com.marublosso.worktrack.worktrack_backend.entity.UserProfiles;
import com.marublosso.worktrack.worktrack_backend.repository.repo.UserProfilesRepository;



@Service
public class UpdateProfileService {

    private final UserProfilesRepository userProfilesRepository;


    public UpdateProfileService(UserProfilesRepository userProfilesRepository) {
        this.userProfilesRepository = userProfilesRepository;
    }


    // 유저 프로필 업데이트
    public void updateProfiles(Long id, SessionUserProfile profilesDto) {

        userProfilesRepository.updateUserProfile(
                UserProfiles.builder()
                        .id(id)
                        .name(profilesDto.getName())
                        .dept(profilesDto.getDept())
                        .build()
        );
    }
}
