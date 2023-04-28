package com.mytask.front.utils;

public enum EStatus {
    TODO,
    IN_PROGRESS,
    DONE;

    public static EStatus getStatus(String status) {
        switch (status) {
            case "TODO":
                return TODO;
            case "IN_PROGRESS":
                return IN_PROGRESS;
            case "DONE":
                return DONE;
            default:
                return TODO;
        }
    }
}