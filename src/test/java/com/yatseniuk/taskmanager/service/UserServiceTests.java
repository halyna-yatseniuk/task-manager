package com.yatseniuk.taskmanager.service;

import com.yatseniuk.taskmanager.documents.User;
import com.yatseniuk.taskmanager.dto.user.UserGeneralDTO;
import com.yatseniuk.taskmanager.exceptions.NotFoundEntityException;
import com.yatseniuk.taskmanager.repository.UserRepository;
import com.yatseniuk.taskmanager.service.implementation.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(UserServiceImpl.class)
public class UserServiceTests {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;

    private User user = new User("5e84767948082934ce3ad5cf", "First name", "Last name",
            "email@gmail.com", "password", LocalDate.parse("2020-08-01"));
    private UserGeneralDTO userGeneralDTO = new UserGeneralDTO("First name", "Last name",
            "email@gmail.com", "password", LocalDate.parse("2020-08-01"));

    @Before
    public void initializeMock() {
        userService = PowerMockito.spy(new UserServiceImpl(userRepository, modelMapper));
    }

    @Test
    public void testGetDTOById() throws Exception {
        PowerMockito.doReturn(user).when(userService, "findById", Mockito.any(String.class));
        Mockito.when(modelMapper.map(user, UserGeneralDTO.class)).thenReturn(userGeneralDTO);
        UserGeneralDTO result = userService.getDTOById(user.getId());
        assertEquals(userGeneralDTO, result);
    }

    @Test
    public void testFindByEmailPassed() {
        Mockito.when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.ofNullable(user));
        User result = userService.findByEmail(user.getEmail());
        assertEquals(user, result);
    }

    @Test(expected = NotFoundEntityException.class)
    public void testFindByEmailFail() {
        Mockito.when(userRepository.findByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());
        userService.findByEmail(user.getEmail());
    }

    @Test
    public void testFindByIdPassed() throws Exception {
        PowerMockito.doReturn(Optional.of(user)).when(userRepository, "findById", Mockito.any(String.class));
        User result = Whitebox.invokeMethod(userService, "findById", user.getId());
        assertEquals(user, result);
    }

    @Test(expected = NotFoundEntityException.class)
    public void testFindByIdFail() throws Exception {
        PowerMockito.doReturn(Optional.empty()).when(userRepository, "findById", Mockito.any(String.class));
        Whitebox.invokeMethod(userService, "findById", user.getId());
    }
}