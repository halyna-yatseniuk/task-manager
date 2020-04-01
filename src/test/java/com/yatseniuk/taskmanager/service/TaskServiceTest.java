package com.yatseniuk.taskmanager.service;

import com.yatseniuk.taskmanager.documents.Task;
import com.yatseniuk.taskmanager.documents.User;
import com.yatseniuk.taskmanager.dto.task.TaskSaveDTO;
import com.yatseniuk.taskmanager.repository.TaskRepository;
import com.yatseniuk.taskmanager.service.implementation.TaskServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TaskServiceImpl.class)
@PowerMockIgnore("javax.security.auth.*")
public class TaskServiceTest {
    @InjectMocks
    private TaskServiceImpl taskService;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserService userService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private Authentication authentication;
    @Mock
    private Principal principal;
    @Mock
    private SecurityContext securityContext;

    private TaskSaveDTO taskSaveDTO = new TaskSaveDTO("Title", "Description");
    private User user = new User("5e84767948082934ce3ad5cf", "First name", "Last name",
            "email@gmail.com", "password", LocalDate.parse("2020-08-01"));
    private Task task = new Task("5e846bea96b1ed5fbe04e9af", "Title", "Description",
            LocalDateTime.parse("2020-08-01T10:15:30"), LocalDateTime.parse("2020-09-04T10:15:30"), user,
            Collections.emptyList());

    @Before
    public void initializeMock() {
        taskService = PowerMockito.spy(new TaskServiceImpl(taskRepository, userService, modelMapper));
    }

    @Test
    public void testSave() {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("");
        when(userService.findByEmail(Mockito.any(String.class))).thenReturn(user);
        taskService.save(taskSaveDTO);
        verify(taskService, times(1)).save(taskSaveDTO);
    }
}