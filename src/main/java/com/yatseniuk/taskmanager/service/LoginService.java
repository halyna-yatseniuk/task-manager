package com.yatseniuk.taskmanager.service;

import com.yatseniuk.taskmanager.dto.token.JwtTokenDTO;
import com.yatseniuk.taskmanager.dto.user.UserLoginDTO;

public interface LoginService {
    JwtTokenDTO login(UserLoginDTO userLoginDTO);
}