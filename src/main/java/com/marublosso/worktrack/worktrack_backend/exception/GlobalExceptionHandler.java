package com.marublosso.worktrack.worktrack_backend.exception;

import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 형식 오류 예외 처리
    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidFormat(InvalidFormatException e) {
        return ResponseEntity
                .badRequest()
                .body(Map.of(
                        "success", false,
                        "message", e.getMessage()));
    }

    // 업무 시간 오류 예외 처리
    @ExceptionHandler(InvalidWorkTimeException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidWorkTime(InvalidWorkTimeException e) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("success", false, "message", e.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccess(DataAccessException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("success", false, "message", "DB 처리에 실패했습니다. 관리자에게 문의하세요."));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "서버 오류가 발생했습니다."));
    }
}
