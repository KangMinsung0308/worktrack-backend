package com.marublosso.worktrack.worktrack_backend.dto;

import lombok.Data;

@Data
public class SignUpRequestDto {

    private Long id;
    private String userEmail;
    private String password;
    private String dept;

}
