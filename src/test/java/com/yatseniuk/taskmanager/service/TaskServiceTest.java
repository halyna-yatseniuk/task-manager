package com.yatseniuk.taskmanager.service;

import com.yatseniuk.taskmanager.dto.tasks.TaskDTO;
import com.yatseniuk.taskmanager.dto.tasks.TaskSaveDTO;
import com.yatseniuk.taskmanager.dto.user.UserGeneralDTO;
import com.yatseniuk.taskmanager.dto.viewPermission.PermissionSaveDTO;
import com.yatseniuk.taskmanager.entity.Task;
import com.yatseniuk.taskmanager.entity.User;
import com.yatseniuk.taskmanager.entity.ViewPermission;
import com.yatseniuk.taskmanager.exceptions.InvalidViewerException;
import com.yatseniuk.taskmanager.exceptions.NotFoundEntityException;
import com.yatseniuk.taskmanager.exceptions.WrongActionException;
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
import org.powermock.reflect.Whitebox;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyPrivate;

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
    private SecurityContext securityContext;

    private User user = new User("5e84767948082934ce3ad5cf", "First name", "Last name",
            "email@gmail.com", "password", LocalDate.parse("2020-08-01"));
    private User viewer = new User("6e84767948082934ce3ad5cf", "Name", "Surname",
            "myemail@gmail.com", "password", LocalDate.parse("2020-08-01"));
    private UserGeneralDTO userGeneralDTO = new UserGeneralDTO("First name", "Last name",
            "email@gmail.com", "password", LocalDate.parse("2020-08-01"));
    private Task task = new Task("5e846bea96b1ed5fbe04e9af", "Title", "Description",
            LocalDateTime.parse("2020-08-01T10:15:30"), LocalDateTime.parse("2020-09-04T10:15:30"), user,
            Collections.emptyList());
    private TaskSaveDTO taskSaveDTO = new TaskSaveDTO("Title", "Description");
    private TaskDTO taskDTO1 = new TaskDTO("Title", "Description", LocalDateTime.parse("2020-08-01T10:15:30"),
            LocalDateTime.parse("2020-09-04T10:15:30"), userGeneralDTO, Collections.emptyList());
    private TaskDTO taskDTO2 = new TaskDTO("Name", "Description", LocalDateTime.parse("2020-09-01T13:45:17"),
            LocalDateTime.parse("2020-09-01T14:23:56"), userGeneralDTO, Collections.emptyList());
    private ViewPermission viewPermission = new ViewPermission(user, viewer, task.getId(),
            LocalDateTime.parse("2020-08-01T10:15:30"));
    private PermissionSaveDTO permissionSaveDTO = new PermissionSaveDTO(task.getId(), viewer.getEmail());
    private List<TaskDTO> taskDTOList1 = new ArrayList<>();
    private List<TaskDTO> taskDTOList2 = new ArrayList<>();
    private List<TaskDTO> taskDTOList = new ArrayList<>();
    private List<Task> tasks = new ArrayList<>();
    private List<String> ids = new ArrayList<>();

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

    @Test
    public void testFindById() throws Exception {
        Mockito.when(taskRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(task));
        Task result = Whitebox.invokeMethod(taskService, "findById", task.getId());
        assertEquals(task, result);
    }

    @Test(expected = NotFoundEntityException.class)
    public void testFindByIdFail() throws Exception {
        Mockito.when(taskRepository.findById(Mockito.any(String.class))).thenReturn(Optional.empty());
        Whitebox.invokeMethod(taskService, "findById", task.getId());
    }

    @Test
    public void testGetDTOById() throws Exception {
        PowerMockito.doReturn(task).when(taskService, "findById", Mockito.any(String.class));
        Mockito.when(modelMapper.map(task, TaskDTO.class)).thenReturn(taskDTO1);
        TaskDTO result = taskService.getDTOById(task.getId());
        assertEquals(taskDTO1, result);
    }

    @Test
    public void testGetAllByUserId() throws Exception {
        taskDTOList1.add(taskDTO1);
        taskDTOList2.add(taskDTO2);
        taskDTOList.add(taskDTO1);
        taskDTOList.add(taskDTO2);

        PowerMockito.doReturn(taskDTOList1).when(taskService, "getAllWhereUserIsOwner", Mockito.any(String.class));
        PowerMockito.doReturn(taskDTOList2).when(taskService, "getAllWhereUserIsViewer", Mockito.any(String.class));
        List<TaskDTO> result = taskService.getAllByUserId(user.getId());
        assertEquals(taskDTOList, result);
    }

    @Test
    public void testGetAllWhereUserIsOwner() {
        taskDTOList1.add(taskDTO1);
        tasks.add(task);

        Mockito.when(taskRepository.findAllByOwnerId(Mockito.any(String.class))).thenReturn(tasks);
        Mockito.when(modelMapper.map(task, TaskDTO.class)).thenReturn(taskDTO1);
        List<TaskDTO> result = taskService.getAllWhereUserIsOwner(user.getId());
        assertEquals(taskDTOList1, result);
    }

    @Test
    public void testGetAllWhereUserIsViewer() {
        tasks.add(task);
        taskDTOList1.add(taskDTO1);

        Mockito.when(taskService.getTasksIdsFromViewPermissions(Mockito.any(String.class))).thenReturn(ids);
        Mockito.when(taskRepository.findAllByIdIsIn(Mockito.anyList())).thenReturn(tasks);
        Mockito.when(modelMapper.map(task, TaskDTO.class)).thenReturn(taskDTO1);
        List<TaskDTO> result = taskService.getAllWhereUserIsViewer(user.getId());
        assertEquals(taskDTOList1, result);
    }

    @Test
    public void testGetTasksIds() {
        tasks.add(task);

        Mockito.when(taskRepository.findAll()).thenReturn(tasks);
        Mockito.when(modelMapper.map(task, TaskDTO.class)).thenReturn(taskDTO1);
        List<String> result = taskService.getTasksIdsFromViewPermissions(user.getId());
        assertEquals(ids, result);
    }

    @Test
    public void testEdit() {
        Mockito.when(taskRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(task));
        taskService.edit(task.getId(), taskSaveDTO);
        verify(taskService, times(1)).edit(task.getId(), taskSaveDTO);
    }

    @Test
    public void testDelete() {
        taskService.delete(task.getId());
        verify(taskRepository, times(1)).deleteById(task.getId());
    }

    @Test(expected = InvalidViewerException.class)
    public void testShareTaskWhoIsViewerOrOwner() throws Exception {
        List<ViewPermission> permissions = new ArrayList<>();
        permissions.add(viewPermission);
        task.setViewers(permissions);

        Mockito.when(taskRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(task));
        PowerMockito.doReturn(true).when(taskService, "isViewerOfTask", permissionSaveDTO, permissions);
        PowerMockito.doReturn(true).when(taskService, "isOwnerOfTask", permissionSaveDTO, task.getOwner());
        taskService.shareTaskWithUser("add", permissionSaveDTO);
    }

    @Test
    public void testShareTaskAddAction() throws Exception {
        List<ViewPermission> permissions = new ArrayList<>();
        permissions.add(viewPermission);
        task.setViewers(permissions);

        Mockito.when(taskRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(task));
        PowerMockito.doReturn(false).when(taskService, "isViewerOfTask",
                Mockito.any(PermissionSaveDTO.class), Mockito.anyList());
        PowerMockito.doReturn(false).when(taskService, "isOwnerOfTask",
                Mockito.any(PermissionSaveDTO.class), Mockito.any(User.class));
        PowerMockito.doReturn(viewPermission).when(taskService, "convertDtoToEntity",
                Mockito.any(PermissionSaveDTO.class));

        taskService.shareTaskWithUser("add", permissionSaveDTO);
        verifyPrivate(taskService, times(1)).
                invoke("convertDtoToEntity", permissionSaveDTO);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testShareTaskRemoveAction() {
        List<ViewPermission> permissions = new ArrayList<>();
        permissions.add(viewPermission);
        task.setViewers(permissions);

        Mockito.when(taskRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(task));
        taskService.shareTaskWithUser("remove", permissionSaveDTO);
        verify(taskRepository, times(1)).save(task);
    }

    @Test(expected = WrongActionException.class)
    public void testShareTaskFail() {
        List<ViewPermission> permissions = new ArrayList<>();
        permissions.add(viewPermission);
        task.setViewers(permissions);

        Mockito.when(taskRepository.findById(Mockito.any(String.class))).thenReturn(Optional.of(task));
        taskService.shareTaskWithUser("anotherAction", permissionSaveDTO);
    }

    @Test
    public void testIsViewerOfTaskTrue() throws Exception {
        Boolean result = Whitebox.invokeMethod(taskService, "isViewerOfTask", permissionSaveDTO,
                Collections.singletonList(viewPermission));
        assertTrue(result);
    }

    @Test
    public void testIsViewerOfTaskFalse() throws Exception {
        permissionSaveDTO.setViewerEmail("email@gmail.com");
        Boolean result = Whitebox.invokeMethod(taskService, "isViewerOfTask", permissionSaveDTO,
                Collections.singletonList(viewPermission));
        assertFalse(result);
    }

    @Test
    public void testIsOwnerOfTaskTrue() throws Exception {
        Boolean result = Whitebox.invokeMethod(taskService, "isOwnerOfTask", permissionSaveDTO, viewer);
        assertTrue(result);
    }

    @Test
    public void testIsOwnerOfTaskFalse() throws Exception {
        Boolean result = Whitebox.invokeMethod(taskService, "isOwnerOfTask", permissionSaveDTO, user);
        assertFalse(result);
    }

    @Test
    public void testConvertDTOToEntity() throws Exception {
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("");
        viewPermission.setViewer(user);
        viewPermission.setSharedDate(null);

        Mockito.when(userService.findByEmail(Mockito.any(String.class))).thenReturn(user);
        ViewPermission result = Whitebox.invokeMethod(taskService,
                "convertDtoToEntity", permissionSaveDTO);
        result.setSharedDate(null);
        assertEquals(viewPermission, result);
    }
}