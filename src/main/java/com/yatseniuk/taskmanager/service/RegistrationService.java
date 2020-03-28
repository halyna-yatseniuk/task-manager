package com.yatseniuk.taskmanager.service;

import com.yatseniuk.taskmanager.dto.UserRegistrationDTO;

public interface RegistrationService {
    void save(UserRegistrationDTO userRegistrationDTO);
}