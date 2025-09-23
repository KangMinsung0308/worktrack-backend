package com.marublosso.worktrack.worktrack_backend.dto;

public class WorkTimeResponseDto {

    private Long userId;
    private String totalTime;

    // Getter & Setter
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getTotalTime() { return totalTime; }
    public void setTotalTime(String totalTime) { this.totalTime = totalTime; }
}
