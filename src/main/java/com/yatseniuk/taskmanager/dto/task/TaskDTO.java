package com.yatseniuk.taskmanager.dto.task;

import com.yatseniuk.taskmanager.dto.user.UserGeneralDTO;
import com.yatseniuk.taskmanager.dto.viewPermission.PermissionGeneralDTO;
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
    private UserGeneralDTO owner;
    private LocalDateTime creationDateTime;
    private LocalDateTime modificationDateTime;
    private Set<PermissionGeneralDTO> viewers;
}