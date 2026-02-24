package com.marublosso.worktrack.worktrack_backend.dto;

import com.marublosso.worktrack.worktrack_backend.entity.auth_profilesJoinEntity;


import lombok.Data;

@Data   
public class LoginUserDto {

    private Long id;
    private String email;
    private String username;
    private String dept;
    public static LoginUserDto from(auth_profilesJoinEntity user) {
        LoginUserDto dto = new LoginUserDto();
        dto.id = user.getId();
        dto.email = user.getEmail();
        dto.username = user.getName();
        dto.dept = user.getDept();
        return dto;
    }

}
