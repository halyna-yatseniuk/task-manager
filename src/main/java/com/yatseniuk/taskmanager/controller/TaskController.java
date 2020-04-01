package com.yatseniuk.taskmanager.controller;

import com.yatseniuk.taskmanager.dto.task.TaskDTO;
import com.yatseniuk.taskmanager.dto.task.TaskSaveDTO;
import com.yatseniuk.taskmanager.dto.viewPermission.PermissionSaveDTO;
import com.yatseniuk.taskmanager.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {
    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public void save(@RequestBody TaskSaveDTO taskSaveDTO) {
        taskService.save(taskSaveDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<TaskDTO>> getAllByUserId(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllByUserId(id));
    }

    @GetMapping("/owner/{id}")
    public ResponseEntity<List<TaskDTO>> getAllWhereUserIsOwner(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllWhereUserIsOwner(id));
    }

    @GetMapping("/viewer/{id}")
    public ResponseEntity<List<TaskDTO>> getAllWhereUserIsViewer(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllWhereUserIsViewer(id));
    }

    @PatchMapping("/{id}")
    public void edit(@PathVariable String id, @RequestBody TaskSaveDTO taskSaveDTO) {
        taskService.edit(id, taskSaveDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        taskService.delete(id);
    }

    @PostMapping("/")
    public void share(@RequestParam String action, @RequestBody PermissionSaveDTO permissionSaveDTO) {
        taskService.shareTaskWithUser(action, permissionSaveDTO);
    }
}