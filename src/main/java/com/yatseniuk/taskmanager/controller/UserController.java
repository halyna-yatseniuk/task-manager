package com.yatseniuk.taskmanager.controller;

import com.yatseniuk.taskmanager.dto.user.UserGeneralDTO;
import com.yatseniuk.taskmanager.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserGeneralDTO> getById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getDTOById(id));
    }
}