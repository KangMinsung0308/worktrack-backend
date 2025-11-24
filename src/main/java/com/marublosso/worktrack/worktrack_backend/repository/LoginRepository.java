package com.marublosso.worktrack.worktrack_backend.repository;

import com.marublosso.worktrack.worktrack_backend.entity.User;

public interface LoginRepository {

    User SearchUserInfo(User user);

}
