package com.yatseniuk.taskmanager.dto.viewPermission;

import com.yatseniuk.taskmanager.dto.task.TaskDTO;
import com.yatseniuk.taskmanager.dto.user.UserGeneralDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionGeneralDTO {
    private String id;
    private LocalDateTime sharedDate;
    private UserGeneralDTO performer;
    private TaskDTO task;
    private UserGeneralDTO viewer;
}