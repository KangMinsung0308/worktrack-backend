package com.marublosso.worktrack.worktrack_backend.dto;

import com.marublosso.worktrack.worktrack_backend.entity.user_authEntity;

import lombok.Data;

@Data   
public class LoginUserDto {

    private Long id;
    private String username;
    private String dept;
    public static LoginUserDto from(user_authEntity user) {
        LoginUserDto dto = new LoginUserDto();
        dto.id = user.getId();
        dto.username = user.getEmail();
        return dto;
    }

}
