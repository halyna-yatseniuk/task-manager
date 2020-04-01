package com.yatseniuk.taskmanager.dto.user;

import com.yatseniuk.taskmanager.constants.Validations;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {
    @NotBlank(message = Validations.EMPTY_FIRST_NAME)
    private String firstName;
    @NotBlank(message = Validations.EMPTY_LAST_NAME)
    private String lastName;
    @NotBlank(message = Validations.EMPTY_EMAIL)
    @Pattern(regexp = Validations.EMAIL_PATTERN,
            message = Validations.INVALID_EMAIL)
    private String email;
    @NotEmpty(message = Validations.EMPTY_PASSWORD)
    private String password;
}