package com.marublosso.worktrack.worktrack_backend.service.biz.java.util.checktools;

import org.springframework.stereotype.Component;

@Component
public class IsVaildEmail {

    public boolean isVaildEmail(String email) {
        // 이메일 유효성 검사 정규식
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }
}
