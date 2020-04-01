package com.yatseniuk.taskmanager.service.implementation;

import com.yatseniuk.taskmanager.constants.ErrorMessages;
import com.yatseniuk.taskmanager.constants.Validations;
import com.yatseniuk.taskmanager.documents.User;
import com.yatseniuk.taskmanager.dto.token.JwtTokenDTO;
import com.yatseniuk.taskmanager.dto.user.UserRegistrationDTO;
import com.yatseniuk.taskmanager.exceptions.BadRegistrationException;
import com.yatseniuk.taskmanager.repository.UserRepository;
import com.yatseniuk.taskmanager.security.TokenManagement;
import com.yatseniuk.taskmanager.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private static final Logger LOG = LoggerFactory.getLogger(RegistrationServiceImpl.class);
    private UserRepository userRepository;
    private TokenManagement tokenManagement;

    @Autowired
    public RegistrationServiceImpl(UserRepository userRepository, TokenManagement tokenManagement) {
        this.userRepository = userRepository;
        this.tokenManagement = tokenManagement;
    }

    @Override
    public JwtTokenDTO save(UserRegistrationDTO userRegistrationDTO) {
        LOG.info("User registration info - {}", userRegistrationDTO);

        if (userRepository.findByEmail(userRegistrationDTO.getEmail()).isPresent()) {
            throw new BadRegistrationException(ErrorMessages.FAIL_TO_REGISTER_A_USER_WITH_EXISTING_EMAIL.getMessage());
        } else {
            User user = User.builder()
                    .firstName(userRegistrationDTO.getFirstName())
                    .lastName(userRegistrationDTO.getLastName())
                    .email(matchEmailToPattern(userRegistrationDTO.getEmail()))
                    .password(userRegistrationDTO.getPassword())
                    .registrationDate(LocalDate.now())
                    .build();
            userRepository.save(user);
        }
        return tokenManagement.generateAccessAndRefreshTokens(userRegistrationDTO.getEmail());
    }

    private String matchEmailToPattern(String email) {
        LOG.info("Matching email to email pattern {}", email);

        Pattern pattern = Pattern.compile(Validations.EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            LOG.error(ErrorMessages.FAIL_TO_REGISTER_USER_WITH_INVALID_EMAIL.getMessage());
            throw new BadRegistrationException(ErrorMessages.FAIL_TO_REGISTER_USER_WITH_INVALID_EMAIL.getMessage());
        }
        return email;
    }
}