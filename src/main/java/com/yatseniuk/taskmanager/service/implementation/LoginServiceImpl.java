package com.yatseniuk.taskmanager.service.implementation;

import com.yatseniuk.taskmanager.constants.ErrorMessages;
import com.yatseniuk.taskmanager.documents.User;
import com.yatseniuk.taskmanager.dto.UserLoginDTO;
import com.yatseniuk.taskmanager.exceptions.BadLoginException;
import com.yatseniuk.taskmanager.repository.UserRepository;
import com.yatseniuk.taskmanager.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {
    private static final Logger LOG = LoggerFactory.getLogger(LoginServiceImpl.class);
    private UserRepository userRepository;

    public LoginServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void login(UserLoginDTO userLoginDTO) {
        LOG.info(userLoginDTO.toString() + " user is signing in");
        Optional<User> user = userRepository.findByEmail(userLoginDTO.getEmail());
        if (user.isPresent()) {
            if (!user.get().getPassword().equals(userLoginDTO.getPassword())) {
                LOG.error(ErrorMessages.FAIL_TO_LOGIN_WITH_WRONG_PASSWORD.getMessage());
                throw new BadLoginException(ErrorMessages.FAIL_TO_LOGIN_WITH_WRONG_PASSWORD.getMessage());
            }
        } else {
            LOG.error(ErrorMessages.FAIL_TO_LOGIN_WITH_WRONG_EMAIL.getMessage());
            throw new BadLoginException(ErrorMessages.FAIL_TO_LOGIN_WITH_WRONG_EMAIL.getMessage());
        }
    }
}