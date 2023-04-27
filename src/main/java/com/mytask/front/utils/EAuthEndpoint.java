package com.mytask.front.utils;

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
