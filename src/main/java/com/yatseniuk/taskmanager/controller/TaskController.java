package com.yatseniuk.taskmanager.controller;

import com.yatseniuk.taskmanager.dto.TaskDTO;
import com.yatseniuk.taskmanager.dto.TaskSaveDTO;
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

    @PatchMapping("/{id}")
    public void edit(@PathVariable String id, @RequestBody TaskSaveDTO taskSaveDTO) {
        taskService.edit(id, taskSaveDTO);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<TaskDTO>> getAll(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllByUserId(id));
    }
}