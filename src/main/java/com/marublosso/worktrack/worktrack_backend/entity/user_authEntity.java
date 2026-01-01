package com.marublosso.worktrack.worktrack_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 전체 필드 생성자

public class user_authEntity {

    private Long id;
    private String email;
    private String password_hash;
    private String created_at;
    private String updated_at;

}
