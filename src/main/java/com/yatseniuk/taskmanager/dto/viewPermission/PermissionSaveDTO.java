package com.yatseniuk.taskmanager.dto.viewPermission;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionSaveDTO {
    private String sharedTaskId;
    private String viewerEmail;
}