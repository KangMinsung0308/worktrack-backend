package com.marublosso.worktrack.worktrack_backend.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfiles {

    private Long id;
    private String name;
    private String dept;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
