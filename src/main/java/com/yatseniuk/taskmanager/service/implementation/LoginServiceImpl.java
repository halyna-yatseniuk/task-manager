package com.yatseniuk.taskmanager.service.implementation;

import com.yatseniuk.taskmanager.constants.ErrorMessages;
import com.yatseniuk.taskmanager.entity.User;
import com.yatseniuk.taskmanager.dto.token.JwtTokenDTO;
import com.yatseniuk.taskmanager.dto.user.UserLoginDTO;
import com.yatseniuk.taskmanager.exceptions.BadLoginException;
import com.yatseniuk.taskmanager.repository.UserRepository;
import com.yatseniuk.taskmanager.security.TokenManagement;
import com.yatseniuk.taskmanager.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    private static final Logger LOG = LoggerFactory.getLogger(LoginServiceImpl.class);
    private UserRepository userRepository;
    private TokenManagement tokenManagement;

    @Autowired
    public LoginServiceImpl(UserRepository userRepository, TokenManagement tokenManagement) {
        this.userRepository = userRepository;
        this.tokenManagement = tokenManagement;
    }

    public JwtTokenDTO login(UserLoginDTO userLoginDTO) {
        LOG.info("User login info - {}", userLoginDTO);

        User user = userRepository.findByEmail(userLoginDTO.getEmail())
                .orElseThrow(() -> new BadLoginException(ErrorMessages.FAIL_TO_LOGIN_WITH_WRONG_EMAIL.getMessage()));

        if (!user.getPassword().equals(userLoginDTO.getPassword())) {
            throw new BadLoginException(ErrorMessages.FAIL_TO_LOGIN_WITH_WRONG_PASSWORD.getMessage());
        }
        return tokenManagement.generateAccessAndRefreshTokens(userLoginDTO.getEmail());
    }
}