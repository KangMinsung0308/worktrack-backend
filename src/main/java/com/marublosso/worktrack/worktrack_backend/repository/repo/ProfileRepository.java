package com.marublosso.worktrack.worktrack_backend.repository.repo;

import com.marublosso.worktrack.worktrack_backend.entity.user_profilesEntity;

public interface ProfileRepository {
    
    // UPDATE
    void updateProfile(user_profilesEntity profileEntity);
}
