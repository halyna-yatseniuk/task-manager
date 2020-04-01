package com.yatseniuk.taskmanager.dto.tasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskSaveDTO {
    private String title;
    private String description;
}