package com.marublosso.worktrack.worktrack_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 전체 필드 생성자

public class User {

    private Long user_id;
    private String username;
    private String password_hash;
    private String name;
    private String dept;
    private String role;
    private String created_at;
    private String updated_at;

}
