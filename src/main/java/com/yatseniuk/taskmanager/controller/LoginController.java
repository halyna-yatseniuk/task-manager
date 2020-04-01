package com.yatseniuk.taskmanager.controller;

import com.yatseniuk.taskmanager.dto.token.JwtTokenDTO;
import com.yatseniuk.taskmanager.dto.user.UserLoginDTO;
import com.yatseniuk.taskmanager.security.TokenManagement;
import com.yatseniuk.taskmanager.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static com.yatseniuk.taskmanager.constants.ConstantValues.*;

import javax.servlet.http.HttpServletResponse;


@RestController
public class LoginController {
    private LoginService loginService;
    private TokenManagement tokenManagement;

    public LoginController(LoginService loginService, TokenManagement tokenManagement) {
        this.loginService = loginService;
        this.tokenManagement = tokenManagement;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response) {

        JwtTokenDTO jwtTokenDTO = loginService.login(userLoginDTO);
        response.setHeader(AUTHORIZATION_HEADER.getValue(),
                AUTH_HEADER_PREFIX.getValue() + jwtTokenDTO.getAccessToken());
        response.setHeader(REFRESH_HEADER.getValue(), jwtTokenDTO.getRefreshToken());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> refresh(@RequestHeader(name = "refreshToken") String refresh,
                                          HttpServletResponse response) {

        JwtTokenDTO refreshedToken = tokenManagement.refreshAccessAndRefreshTokens(refresh);
        response.setHeader(AUTHORIZATION_HEADER.getValue(),
                AUTH_HEADER_PREFIX.getValue() + refreshedToken.getAccessToken());
        response.setHeader(REFRESH_HEADER.getValue(), refreshedToken.getRefreshToken());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}