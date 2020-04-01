package com.yatseniuk.taskmanager.service.implementation;

import com.yatseniuk.taskmanager.constants.ErrorMessages;
import com.yatseniuk.taskmanager.documents.Task;
import com.yatseniuk.taskmanager.documents.User;
import com.yatseniuk.taskmanager.documents.ViewPermission;
import com.yatseniuk.taskmanager.dto.task.TaskDTO;
import com.yatseniuk.taskmanager.dto.task.TaskSaveDTO;
import com.yatseniuk.taskmanager.dto.viewPermission.PermissionSaveDTO;
import com.yatseniuk.taskmanager.exceptions.InvalidViewerException;
import com.yatseniuk.taskmanager.exceptions.NotFoundEntityException;
import com.yatseniuk.taskmanager.exceptions.WrongActionException;
import com.yatseniuk.taskmanager.repository.TaskRepository;
import com.yatseniuk.taskmanager.service.TaskService;
import com.yatseniuk.taskmanager.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);
    private TaskRepository taskRepository;
    private UserService userService;
    private ModelMapper modelMapper;
    private Principal principal;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserService userService, ModelMapper modelMapper) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void save(TaskSaveDTO taskSaveDTO) {
        LOG.info("Creating a new task - {}", taskSaveDTO);
        principal = SecurityContextHolder.getContext().getAuthentication();
        LOG.info("Currently logged user name - {}", principal);

        Task task = Task.builder()
                .title(taskSaveDTO.getTitle())
                .description(taskSaveDTO.getDescription())
                .owner(userService.findByEmail(principal.getName()))
                .creationDateTime(LocalDateTime.now())
                .viewers(Collections.emptyList())
                .build();
        taskRepository.save(task);
    }

    @Override
    public TaskDTO getById(String id) {
        LOG.info("Getting a task with id - {}", id);
        return modelMapper.map(findById(id), TaskDTO.class);
    }

    @Override
    public List<TaskDTO> getAllByUserId(String userId) {
        LOG.info("Search all tasks for user with id - {}", userId);
        List<TaskDTO> allTasks = getAllWhereUserIsOwner(userId);
        allTasks.addAll(getAllWhereUserIsViewer(userId));
        return allTasks;
    }

    public List<TaskDTO> getAllWhereUserIsOwner(String userId) {
        LOG.info("Search all tasks by user id where user is a creator -{}", userId);
        List<Task> tasks = taskRepository.findAllByOwnerId(userId);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .collect(Collectors.toList());
    }

    public List<TaskDTO> getAllWhereUserIsViewer(String userId) {
        LOG.info("Search all tasks by user id where user is a viewer -{}", userId);
        List<String> ids = gettingTaskIdsFromViewPermissions(userId);
        List<Task> tasks = taskRepository.findAllByIdIsIn(ids);
        return tasks.stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .collect(Collectors.toList());
    }

    public List<String> gettingTaskIdsFromViewPermissions(String userId) {
        LOG.info("Search task ids from all view permissions where user is a viewer");
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(Task::getViewers)
                .flatMap(Collection::stream)
                .filter(f -> f.getViewer().getId().equals(userId))
                .map(ViewPermission::getTaskId)
                .collect(Collectors.toList());
    }

    @Override
    public void edit(String id, TaskSaveDTO taskSaveDTO) {
        LOG.info("Editing a task with id - {}", id);
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
        LOG.info("Deleting a task with id - {}", id);
        taskRepository.deleteById(id);
    }

    @Override
    public void shareTaskWithUser(String action, PermissionSaveDTO permissionSaveDTO) {
        Task task = findById(permissionSaveDTO.getSharedTaskId());
        List<ViewPermission> viewers = task.getViewers();

        switch (action) {
            case "add":
                if (isViewerOfTask(permissionSaveDTO, viewers) || isOwnerOfTask(permissionSaveDTO, task.getOwner())) {
                    throw new InvalidViewerException(ErrorMessages.USER_ALREADY_ACCESS_TASK.getMessage());
                } else {
                    LOG.info("Share task with a user - {}", permissionSaveDTO.getViewerEmail());
                    viewers.add(convertDtoToEntity(permissionSaveDTO));
                }
                break;
            case "remove":
                LOG.info("Remove task for a user - {}", permissionSaveDTO.getViewerEmail());
                viewers.removeIf(viewer -> viewer.getViewer().getEmail().equals(permissionSaveDTO.getViewerEmail()));
                break;
            default:
                throw new WrongActionException(ErrorMessages.WRONG_TASK_ACTION.getMessage());
        }

        task.setViewers(viewers);
        task.setModificationDateTime(LocalDateTime.now());
        taskRepository.save(task);
    }

    private Boolean isViewerOfTask(PermissionSaveDTO permissionSaveDTO, List<ViewPermission> viewers) {
        principal = SecurityContextHolder.getContext().getAuthentication();
        return viewers.stream()
                .map(m -> m.getViewer().getEmail())
                .anyMatch(r -> r.equals(permissionSaveDTO.getViewerEmail()));
    }

    private Boolean isOwnerOfTask(PermissionSaveDTO permissionSaveDTO, User taskOwner) {
        return permissionSaveDTO.getViewerEmail().equals(taskOwner.getEmail());
    }

    private ViewPermission convertDtoToEntity(PermissionSaveDTO permissionSaveDTO) {
        principal = SecurityContextHolder.getContext().getAuthentication();
        return ViewPermission.builder()
                .sharedDate(LocalDateTime.now())
                .performer(userService.findByEmail(principal.getName()))
                .taskId(permissionSaveDTO.getSharedTaskId())
                .viewer(userService.findByEmail(permissionSaveDTO.getViewerEmail()))
                .build();
    }

    private Task findById(String id) {
        LOG.info("Finding a task with id - {}", id);
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(ErrorMessages.FAIL_TO_FIND_A_TASK.getMessage()));
    }
}