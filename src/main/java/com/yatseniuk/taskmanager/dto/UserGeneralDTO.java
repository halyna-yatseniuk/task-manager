package com.yatseniuk.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGeneralDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<TaskDTO> taskSaveDTOS;
}