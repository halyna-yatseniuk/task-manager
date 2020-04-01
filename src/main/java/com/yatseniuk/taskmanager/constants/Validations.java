package com.yatseniuk.taskmanager.constants;

public interface Validations {
    String EMAIL_PATTERN = "^\\s*[a-zA-Z0-9]+((\\.|_|-)?[a-zA-Z0-9])+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}\\s*$";
    String INVALID_EMAIL = "Invalid";
    String EMPTY_FIRST_NAME = "First name should not be blank";
    String EMPTY_LAST_NAME = "Last name should not be blank";
    String EMPTY_EMAIL = "Email should not be blank";
    String EMPTY_PASSWORD = "Password should not be blank";
}