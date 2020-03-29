package com.yatseniuk.taskmanager.service;

import com.yatseniuk.taskmanager.documents.User;
import com.yatseniuk.taskmanager.dto.UserGeneralDTO;

public interface UserService {
    User findById(String id);

    UserGeneralDTO getById(String id);
}