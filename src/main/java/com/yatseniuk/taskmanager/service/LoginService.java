package com.yatseniuk.taskmanager.service;

import com.yatseniuk.taskmanager.dto.UserLoginDTO;

public interface LoginService {
    void login(UserLoginDTO userLoginDTO);
}