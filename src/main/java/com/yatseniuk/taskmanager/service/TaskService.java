package com.yatseniuk.taskmanager.service;

import com.yatseniuk.taskmanager.dto.task.TaskDTO;
import com.yatseniuk.taskmanager.dto.task.TaskSaveDTO;

import java.util.List;

public interface TaskService {
    void save(TaskSaveDTO taskSaveDTO);

    TaskDTO getById(String id);

    List<TaskDTO> getAllByUserId(String id);

    void edit(String id, TaskSaveDTO taskSaveDTO);

    void delete(String id);

    void shareTaskWithUser(String userId, String taskId, String action);
}