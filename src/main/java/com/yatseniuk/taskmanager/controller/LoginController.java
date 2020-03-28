package com.yatseniuk.taskmanager.controller;

import com.yatseniuk.taskmanager.dto.UserLoginDTO;
import com.yatseniuk.taskmanager.service.LoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public void login(@RequestBody UserLoginDTO userLoginDTO) {
        loginService.login(userLoginDTO);
    }
}