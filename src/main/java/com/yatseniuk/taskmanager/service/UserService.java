package com.yatseniuk.taskmanager.service;

import com.yatseniuk.taskmanager.dto.UserGeneralDTO;

public interface UserService {
    UserGeneralDTO getById(String id);
}