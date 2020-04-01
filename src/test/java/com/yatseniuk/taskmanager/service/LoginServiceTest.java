package com.yatseniuk.taskmanager.service;

import com.yatseniuk.taskmanager.entity.User;
import com.yatseniuk.taskmanager.dto.token.JwtTokenDTO;
import com.yatseniuk.taskmanager.dto.user.UserLoginDTO;
import com.yatseniuk.taskmanager.exceptions.BadLoginException;
import com.yatseniuk.taskmanager.repository.UserRepository;
import com.yatseniuk.taskmanager.security.TokenManagement;
import com.yatseniuk.taskmanager.service.implementation.LoginServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LoginServiceImpl.class)
public class LoginServiceTest {
    @InjectMocks
    private LoginServiceImpl loginService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenManagement tokenManagement;

    private User user = new User("5e84767948082934ce3ad5cf", "First name", "Last name",
            "email@gmail.com", "password", LocalDate.parse("2020-08-01"));
    private UserLoginDTO userLoginDTO = new UserLoginDTO("email@gmail.com", "password");
    private JwtTokenDTO token = new JwtTokenDTO("accessToken", "refreshToken");

    @Before
    public void initializeMock() {
        loginService = PowerMockito.spy(new LoginServiceImpl(userRepository, tokenManagement));
    }

    @Test
    public void testLoginSuccess() {
        Mockito.when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));
        Mockito.when(tokenManagement.generateAccessAndRefreshTokens(Mockito.any(String.class))).thenReturn(token);
        JwtTokenDTO jwtTokenDTO = loginService.login(userLoginDTO);
        assertEquals(jwtTokenDTO, token);
    }

    @Test(expected = BadLoginException.class)
    public void testLoginWithWrongEmail() {
        Mockito.when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());
        loginService.login(userLoginDTO);
    }

    @Test(expected = BadLoginException.class)
    public void testLoginWithWrongPassword() {
        userLoginDTO.setPassword("anotherPassword");
        Mockito.when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));
        loginService.login(userLoginDTO);
    }
}