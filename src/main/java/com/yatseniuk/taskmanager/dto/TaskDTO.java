package com.yatseniuk.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private String title;
    private String description;
    private String owner;
    private LocalDateTime creationDateTime;
    private LocalDateTime modificationDateTime;
    private Set<UserGeneralDTO> viewers;
}