package com.marublosso.worktrack.worktrack_backend.repository.repo;

import com.marublosso.worktrack.worktrack_backend.entity.UserProfiles;

public interface UserProfilesRepository {

    // insert user profile
    void insertUserProfile(UserProfiles userProfiles);

    // Update user profile
    void updateUserProfile(UserProfiles userProfiles);

    // select user profile by id
    UserProfiles selectUserProfileById(Long id);

}
