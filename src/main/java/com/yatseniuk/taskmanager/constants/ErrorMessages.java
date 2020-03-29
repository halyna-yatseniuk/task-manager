package com.yatseniuk.taskmanager.constants;

public enum ErrorMessages {
    FAIL_TO_REGISTER_A_USER_WITH_EXISTING_EMAIL("User with such email is already registered"),
    FAIL_TO_REGISTER_USER_WITH_INVALID_EMAIL("Email you are using is invalid"),

    FAIL_TO_LOGIN_WITH_WRONG_EMAIL("User with entered email has not created yet"),
    FAIL_TO_LOGIN_WITH_WRONG_PASSWORD("Entered password is not correct"),

    FAIL_TO_FIND_A_USER("There is no such a user"),
    FAIL_TO_FIND_A_TASK("There is no such a task");

    private String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}