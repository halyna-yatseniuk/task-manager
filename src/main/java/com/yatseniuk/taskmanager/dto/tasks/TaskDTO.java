package com.yatseniuk.taskmanager.dto.tasks;

import com.yatseniuk.taskmanager.dto.user.UserGeneralDTO;
import com.yatseniuk.taskmanager.dto.viewPermission.PermissionGeneralDTO;
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
    private LocalDateTime creationDateTime;
    private LocalDateTime modificationDateTime;
    private UserGeneralDTO owner;
    private List<PermissionGeneralDTO> viewers;
}