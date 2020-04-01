package com.yatseniuk.taskmanager.constants;

public class Validations {
    public static final String EMAIL_PATTERN =
            "^\\s*[a-zA-Z0-9]+((\\.|_|-)?[a-zA-Z0-9])+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,4}\\s*$";
    public static final String EMPTY_FIRST_NAME = "First name should not be blank";
    public static final String EMPTY_LAST_NAME = "Last name should not be blank";
    public static final String EMPTY_EMAIL = "Email should not be blank";
    public static final String EMPTY_PASSWORD = "Password should not be blank";
}