package com.yatseniuk.taskmanager.service.implementation;

import com.yatseniuk.taskmanager.constants.ConstantValues;
import com.yatseniuk.taskmanager.constants.ErrorMessages;
import com.yatseniuk.taskmanager.documents.Task;
import com.yatseniuk.taskmanager.documents.User;
import com.yatseniuk.taskmanager.dto.TaskDTO;
import com.yatseniuk.taskmanager.dto.TaskSaveDTO;
import com.yatseniuk.taskmanager.exceptions.NotFoundEntityException;
import com.yatseniuk.taskmanager.exceptions.WrongActionException;
import com.yatseniuk.taskmanager.repository.TaskRepository;
import com.yatseniuk.taskmanager.service.TaskService;
import com.yatseniuk.taskmanager.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);
    private TaskRepository taskRepository;
    private UserService userService;
    private ModelMapper modelMapper;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserService userService, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void save(TaskSaveDTO taskSaveDTO) {
        LOG.info("Creating a new task");
        Task task = Task.builder()
                .title(taskSaveDTO.getTitle())
                .description(taskSaveDTO.getDescription())
                .owner(userService.findById(taskSaveDTO.getUserId()))
                .creationDateTime(LocalDateTime.now())
                .viewers(Collections.emptySet())
                .build();
        taskRepository.save(task);
    }

    @Override
    public TaskDTO getById(String id) {
        LOG.info("Getting a task with id " + id);
        return modelMapper.map(findById(id), TaskDTO.class);
    }

    @Override
    public List<TaskDTO> getAllByUserId(String id) {
        LOG.info("Search all tasks for user with id " + id);
        List<Task> tasks = taskRepository.findAllByOwnerId(id);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getAllWhereUserIsOwner(String id) {
        return null;
    }

    public List<TaskDTO> getAllWhereUserIsViewer(String id) {
        return null;
    }

    @Override
    public void edit(String id, TaskSaveDTO taskSaveDTO) {
        LOG.info("Editing a task with id " + id);
        Task task = findById(id);
        if (!taskSaveDTO.getTitle().isEmpty()) {
            task.setTitle(taskSaveDTO.getTitle());
            task.setModificationDateTime(LocalDateTime.now());
        }
        if (!taskSaveDTO.getDescription().isEmpty()) {
            task.setDescription(taskSaveDTO.getDescription());
            task.setModificationDateTime(LocalDateTime.now());
        }
        taskRepository.save(task);
    }

    @Override
    public void delete(String id) {
        LOG.info("Deleting a task with id " + id);
        taskRepository.deleteById(id);
    }

    @Override
    public void shareTaskWithUser(String userId, String taskId, String action) {
        Task task = findById(taskId);
        Set<User> viewers = task.getViewers();
        if (action.equals(ConstantValues.ADD.getValue())) {
            LOG.info("Adding a user with id " + userId + " as a viewer to task with id " + taskId);
            viewers.add(userService.findById(userId));
        } else if (action.equals(ConstantValues.REMOVE.getValue())) {
            LOG.info("Removing a user with id " + userId + " from viewers to task with id " + taskId);
            viewers.removeIf(viewer -> viewer.getId().equals(userId));
        } else {
            LOG.error(ErrorMessages.WRONG_TASK_ACTION.getMessage());
            throw new WrongActionException(ErrorMessages.WRONG_TASK_ACTION.getMessage());
        }
        task.setViewers(viewers);
        task.setModificationDateTime(LocalDateTime.now());
        taskRepository.save(task);
    }

    private Task findById(String id) {
        LOG.info("Finding a task with id " + id);
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(ErrorMessages.FAIL_TO_FIND_A_TASK.getMessage()));
    }
}