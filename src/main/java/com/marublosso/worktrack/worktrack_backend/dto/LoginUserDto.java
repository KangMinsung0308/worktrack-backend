package com.marublosso.worktrack.worktrack_backend.dto;

import lombok.Data;

@Data
public class LoginUserDto {

    private Long id;
    private String username;
    private String email;
    private String password;

}
