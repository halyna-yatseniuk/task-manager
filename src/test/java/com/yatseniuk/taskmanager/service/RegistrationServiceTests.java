package com.yatseniuk.taskmanager.service;

import com.yatseniuk.taskmanager.entity.User;
import com.yatseniuk.taskmanager.dto.token.JwtTokenDTO;
import com.yatseniuk.taskmanager.dto.user.UserRegistrationDTO;
import com.yatseniuk.taskmanager.exceptions.BadRegistrationException;
import com.yatseniuk.taskmanager.repository.UserRepository;
import com.yatseniuk.taskmanager.security.TokenManagement;
import com.yatseniuk.taskmanager.service.implementation.RegistrationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RegistrationServiceImpl.class)
public class RegistrationServiceTests {
    @InjectMocks
    private RegistrationServiceImpl registrationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenManagement tokenManagement;

    private User user = new User("5e84767948082934ce3ad5cf", "First name", "Last name",
            "email@gmail.com", "password", LocalDate.parse("2020-08-01"));
    private UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO("First name", "Last name",
            "email@gmail.com", "password");
    private JwtTokenDTO token = new JwtTokenDTO("accessToken", "refreshToken");

    @Before
    public void initializeMock() {
        registrationService = PowerMockito.spy(new RegistrationServiceImpl(userRepository, tokenManagement));
    }

    @Test
    public void testSavePassed() {
        Mockito.when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        Mockito.when(tokenManagement.generateAccessAndRefreshTokens(Mockito.any(String.class))).thenReturn(token);
        JwtTokenDTO jwtTokenDTO = registrationService.save(userRegistrationDTO);
        assertEquals(jwtTokenDTO, token);
    }

    @Test(expected = BadRegistrationException.class)
    public void testSaveFail() {
        Mockito.when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));
        registrationService.save(userRegistrationDTO);
    }

    @Test
    public void testMatchEmailToPatternPassed() throws Exception {
        String result = Whitebox.invokeMethod(registrationService, "matchEmailToPattern",
                user.getEmail());
        assertEquals(result, user.getEmail());
    }

    @Test(expected = BadRegistrationException.class)
    public void testMatchEmailToPatternFailed() throws Exception {
        String invalidEmail = "invalidEmail";
        Whitebox.invokeMethod(registrationService, "matchEmailToPattern",
                invalidEmail);
    }
}