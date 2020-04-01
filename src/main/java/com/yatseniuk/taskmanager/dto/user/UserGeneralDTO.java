package com.yatseniuk.taskmanager.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserGeneralDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDate registrationDate;
}