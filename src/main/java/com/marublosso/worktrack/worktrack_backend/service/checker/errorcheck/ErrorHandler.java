package com.marublosso.worktrack.worktrack_backend.service.checker.errorcheck;

import org.springframework.stereotype.Component;

@Component
public class ErrorHandler {

    public String handleError(int errorCode) {
    // 간단한 에러 처리 로직 (예: 로그 출력)

    if(errorCode == 1) {
        String errorMessage = "출근시간이나 퇴근시간이 입력되어있지 않습니다.";
        System.err.println("에러 핸들러 디버그: " + errorMessage);
        return errorMessage;
    }
    if(errorCode == 2) {
        String errorMessage = "[퇴근시간]은 출근시간 이후여야 합니다"; 
        System.err.println("에러 핸들러 디버그: " + errorMessage); 
        return errorMessage;
    }

    return null; // 에러 없음
    }
}
