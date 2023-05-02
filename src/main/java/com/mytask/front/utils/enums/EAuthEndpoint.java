package com.mytask.front.utils.enums;

public enum EAuthEndpoint {
    LOGIN("login"),
    REGISTER("register");

    private final String endpoint;

    EAuthEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getValue() {
        return endpoint;
    }
}
