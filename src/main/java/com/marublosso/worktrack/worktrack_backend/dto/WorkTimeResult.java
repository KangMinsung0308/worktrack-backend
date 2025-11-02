package com.marublosso.worktrack.worktrack_backend.dto;

public class WorkTimeResult {
    private double totalHours;
    private double overtimeHours;

    public WorkTimeResult(double totalHours, double overtimeHours) {
        this.totalHours = totalHours;
        this.overtimeHours = overtimeHours;
    }

    // getter
    public double getTotalHours() { return totalHours; }
    public double getOvertimeHours() { return overtimeHours; }
}
