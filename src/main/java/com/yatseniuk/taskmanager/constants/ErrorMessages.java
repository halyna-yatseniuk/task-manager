package com.yatseniuk.taskmanager.constants;

public enum ErrorMessages {
    FAIL_TO_REGISTER_A_USER_WITH_EXISTING_EMAIL("User with such email is already registered"),
    FAIL_TO_REGISTER_USER_WITH_INVALID_EMAIL("Email you are using is invalid"),

    FAIL_TO_LOGIN_WITH_WRONG_EMAIL("User with entered email has not created yet"),
    FAIL_TO_LOGIN_WITH_WRONG_PASSWORD("Entered password is not correct"),

    FAIL_TO_FIND_A_USER("There is no such a user"),
    NOT_FOUND_USER_WITH_ENTERED_EMAIL("User with entered email is not found"),
    FAIL_TO_FIND_A_TASK("There is no such a task"),

    WRONG_TASK_ACTION("Wrong action was used while sharing task"),
    USER_ALREADY_ACCESS_TASK("User already has ability to view current task"),

    UNAUTHORIZED("You have to be authorized to perform the action"),
    EXPIRED_TOKEN("Token is expired"),
    FAIL_TO_AUTHENTICATE("JWT Authentication failed"),
    FAIL_TO_REFRESH_TOKEN("JWT refresh failed"),
    INVALID_TOKEN("Token is not valid"),
    CANNOT_RETRIEVE_USER_DATA("Fail to retrieve username from token");

    private String message;

    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}