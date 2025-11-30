package com.marublosso.worktrack.worktrack_backend.dto;

import com.marublosso.worktrack.worktrack_backend.entity.User;

import lombok.Data;

@Data   
public class LoginUserDto {

    private Long id;
    private String username;
    private String dept;
    public static LoginUserDto from(User user) {
        LoginUserDto dto = new LoginUserDto();
        dto.id = user.getUser_id();
        dto.username = user.getUsername();
        dto.dept = user.getDept();
        return dto;
    }

}
