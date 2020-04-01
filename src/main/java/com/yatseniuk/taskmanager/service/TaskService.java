package com.yatseniuk.taskmanager.service;

import com.yatseniuk.taskmanager.dto.tasks.TaskDTO;
import com.yatseniuk.taskmanager.dto.tasks.TaskSaveDTO;
import com.yatseniuk.taskmanager.dto.viewPermission.PermissionSaveDTO;

import java.util.List;

public interface TaskService {
    void save(TaskSaveDTO taskSaveDTO);

    TaskDTO getDTOById(String id);

    List<TaskDTO> getAllByUserId(String id);

    List<TaskDTO> getAllWhereUserIsOwner(String id);

    List<TaskDTO> getAllWhereUserIsViewer(String id);

    void edit(String id, TaskSaveDTO taskSaveDTO);

    void delete(String id);

    void shareTaskWithUser(String action, PermissionSaveDTO permissionSaveDTO);
}