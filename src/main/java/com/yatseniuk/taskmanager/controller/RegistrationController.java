package com.yatseniuk.taskmanager.controller;

import com.yatseniuk.taskmanager.dto.user.UserRegistrationDTO;
import com.yatseniuk.taskmanager.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
    private RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/sign-up")
    public void register(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        registrationService.save(userRegistrationDTO);
    }
}