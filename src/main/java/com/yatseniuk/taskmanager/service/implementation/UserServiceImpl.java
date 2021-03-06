package com.yatseniuk.taskmanager.service.implementation;

import com.yatseniuk.taskmanager.constants.ErrorMessages;
import com.yatseniuk.taskmanager.entity.User;
import com.yatseniuk.taskmanager.dto.user.UserGeneralDTO;
import com.yatseniuk.taskmanager.exceptions.NotFoundEntityException;
import com.yatseniuk.taskmanager.repository.UserRepository;
import com.yatseniuk.taskmanager.service.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserGeneralDTO getDTOById(String id) {
        LOG.info("Getting a user with id - {}", id);
        return modelMapper.map(findById(id), UserGeneralDTO.class);
    }

    @Override
    public User findByEmail(String email) {
        LOG.info("Getting a user with email - {} ", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundEntityException(
                        ErrorMessages.NOT_FOUND_USER_WITH_ENTERED_EMAIL.getMessage()));
    }

    private User findById(String id) {
        LOG.info("Finding a user with id - {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(ErrorMessages.FAIL_TO_FIND_A_USER.getMessage()));
    }
}