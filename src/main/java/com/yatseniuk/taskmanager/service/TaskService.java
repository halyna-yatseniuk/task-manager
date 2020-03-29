package com.yatseniuk.taskmanager.service;

import com.yatseniuk.taskmanager.dto.TaskDTO;
import com.yatseniuk.taskmanager.dto.TaskSaveDTO;

import java.util.List;

public interface TaskService {
    void save(TaskSaveDTO taskSaveDTO);

    TaskDTO getById(String id);

    List<TaskDTO> getAllByUserId(String id);

    void edit(String id, TaskSaveDTO taskSaveDTO);

    void delete(String id);

    void shareWithUser(String userId, String taskId);
}