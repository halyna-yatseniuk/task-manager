package com.yatseniuk.taskmanager.service;

import com.yatseniuk.taskmanager.dto.token.JwtTokenDTO;
import com.yatseniuk.taskmanager.dto.user.UserRegistrationDTO;

public interface RegistrationService {
    JwtTokenDTO save(UserRegistrationDTO userRegistrationDTO);
}