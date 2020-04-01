package com.yatseniuk.taskmanager.service;

import com.yatseniuk.taskmanager.entity.User;
import com.yatseniuk.taskmanager.dto.user.UserGeneralDTO;

public interface UserService {
    UserGeneralDTO getDTOById(String id);

    User findByEmail(String email);
}