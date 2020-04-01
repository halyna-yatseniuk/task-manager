package com.yatseniuk.taskmanager.controller;

import com.yatseniuk.taskmanager.dto.token.JwtTokenDTO;
import com.yatseniuk.taskmanager.dto.user.UserRegistrationDTO;
import com.yatseniuk.taskmanager.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.yatseniuk.taskmanager.constants.ConstantValues.*;

@RestController
public class RegistrationController {
    private RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Object> register(@Valid @RequestBody UserRegistrationDTO userRegistrationDTO,
                                           HttpServletResponse response) {
        JwtTokenDTO jwtTokenDTO = registrationService.save(userRegistrationDTO);
        response.setHeader(AUTHORIZATION_HEADER.getValue(),
                AUTH_HEADER_PREFIX.getValue() + jwtTokenDTO.getAccessToken());
        response.setHeader(REFRESH_HEADER.getValue(), jwtTokenDTO.getRefreshToken());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}