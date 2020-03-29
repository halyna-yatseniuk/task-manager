package com.yatseniuk.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {
    private String title;
    private String description;
    private String ownerId;
    private LocalDateTime creationDateTime;
    private LocalDateTime modificationDateTime;
    private List<UserRegistrationDTO> viewers;
}