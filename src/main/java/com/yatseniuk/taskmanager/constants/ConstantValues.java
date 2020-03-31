package com.yatseniuk.taskmanager.constants;

public enum ConstantValues {
    ADD("add"),
    REMOVE("remove"),
    GLOBAL_SCOPE("global"),
    SCOPE_DESCRIPTION("accessEverything"),
    JWT("JWT"),
    HEADER("header"),
    AUTH_HEADER_PREFIX("Bearer "),
    AUTHORIZATION_HEADER("authorization"),
    REFRESH_HEADER("refreshToken");

    private String value;

    ConstantValues(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}