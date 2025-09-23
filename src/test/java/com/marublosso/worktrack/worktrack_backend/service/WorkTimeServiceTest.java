package com.marublosso.worktrack.worktrack_backend.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.jdbc.core.JdbcTemplate;

class WorkTimeServiceTest {

    private JdbcTemplate jdbcTemplate;
    private WorkTimeService workTimeService;

    @BeforeEach
    void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        workTimeService = new WorkTimeService(jdbcTemplate);
    }

    @Test
    void recordWorkTime_정상호출_파라미터검증() {
        Long userId = 1L;
        LocalDate workDate = LocalDate.of(2024, 6, 1);
        LocalDateTime startTime = LocalDateTime.of(2024, 6, 1, 9, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 6, 1, 18, 0);
        Double totalHours = 8.0;
        Double overtime = 1.0;

        workTimeService.recordWorkTime(userId, workDate, startTime, endTime, totalHours, overtime);

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> paramCaptor = ArgumentCaptor.forClass(Object.class);

        verify(jdbcTemplate, times(1)).update(
            sqlCaptor.capture(),
            paramCaptor.capture(),
            paramCaptor.capture(),
            paramCaptor.capture(),
            paramCaptor.capture(),
            paramCaptor.capture(),
            paramCaptor.capture(),
            paramCaptor.capture(),
            paramCaptor.capture()
        );

        String expectedSql = "INSERT INTO work_time " +
                "(user_id, work_date, start_time, end_time, total_hours, overtime, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        assertEquals(expectedSql, sqlCaptor.getValue());

        Object[] params = paramCaptor.getAllValues().toArray();
        assertEquals(userId, params[0]);
        assertEquals(workDate, params[1]);
        assertEquals(startTime, params[2]);
        assertEquals(endTime, params[3]);
        assertEquals(totalHours, params[4]);
        assertEquals(overtime, params[5]);
        assertNotNull(params[6]); // created_at
        assertNotNull(params[7]); // updated_at
    }

    @Test
    void getWorkTime_미구현_null반환() {
        Object result = workTimeService.getWorkTime(1L);
        assertNull(result);
    }
}