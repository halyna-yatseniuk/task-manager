package com.yatseniuk.taskmanager.security;

import com.yatseniuk.taskmanager.constants.ErrorMessages;
import com.yatseniuk.taskmanager.exceptions.NotFoundEntityException;
import com.yatseniuk.taskmanager.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        com.yatseniuk.taskmanager.documents.User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundEntityException(ErrorMessages.FAIL_TO_FIND_A_USER.getMessage()));
        return new User(user.getEmail(), user.getPassword(), emptyList());
    }
}