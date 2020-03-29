package com.yatseniuk.taskmanager.service.implementation;

import com.yatseniuk.taskmanager.constants.ErrorMessages;
import com.yatseniuk.taskmanager.documents.Task;
import com.yatseniuk.taskmanager.documents.User;
import com.yatseniuk.taskmanager.dto.TaskDTO;
import com.yatseniuk.taskmanager.dto.TaskSaveDTO;
import com.yatseniuk.taskmanager.exceptions.NotFoundEntityException;
import com.yatseniuk.taskmanager.repository.TaskRepository;
import com.yatseniuk.taskmanager.repository.UserRepository;
import com.yatseniuk.taskmanager.service.TaskService;
import com.yatseniuk.taskmanager.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);
    private TaskRepository taskRepository;
    private UserService userService;
    private ModelMapper modelMapper;
    private UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserService userService, ModelMapper modelMapper,
                           UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @Override
    public void save(TaskSaveDTO taskSaveDTO) {
        LOG.info("Creating a new task");
        Task task = Task.builder()
                .title(taskSaveDTO.getTitle())
                .description(taskSaveDTO.getDescription())
                .owner(userService.findById(taskSaveDTO.getUserId()))
                .creationDateTime(LocalDateTime.now())
                .build();
        taskRepository.save(task);
    }

    @Override
    public TaskDTO getById(String id) {
        return modelMapper.map(findById(id), TaskDTO.class);
    }

    @Override
    public List<TaskDTO> getAllByUserId(String id) {
        List<Task> tasks = taskRepository.findAllByOwnerId(id);
        LOG.info("!!!!!!!!!!!!!!!!!!" + tasks);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void edit(String id, TaskSaveDTO taskSaveDTO) {
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

    }

    @Override
    public void shareWithUser(String userId, String taskId) {

    }

    private Task findById(String id) {
        LOG.info("Finding a task with id " + id);
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(ErrorMessages.FAIL_TO_FIND_A_TASK.getMessage()));
    }
}