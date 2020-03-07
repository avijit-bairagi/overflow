package com.mrrobot.overflow.common.utils;

public enum ResponseStatus {

    SUCCESS("1000", "Success"),
    ALREADY_EXITS("1404", "Already Exits"),
    BAD_CREDENTIALS("1401", "Bad credentials"),
    AUTH_ERROR("1111", "Auth error");

    private final String value;
    private final String description;

    ResponseStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String value() {
        return value;
    }

    public String description() {
        return description;
    }
}
