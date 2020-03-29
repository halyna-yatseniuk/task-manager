package com.yatseniuk.taskmanager.constants;

public enum ConstantValues {
    ADD("add"),
    REMOVE("remove");

    private String value;

    ConstantValues(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}