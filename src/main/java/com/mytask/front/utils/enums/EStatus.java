package com.mytask.front.utils.enums;

public enum EStatus {
    TODO,
    IN_PROGRESS("IN PROGRESS"),
    DONE;

    public static String getStatus(String status) {
        switch (status) {
            case "TODO":
                return TODO.getValue();
            case "IN PROGRESS":
                return IN_PROGRESS.getValue();
            case "DONE":
                return DONE.getValue();
            default:
                return "";
        }
    }

    public static EStatus getStatusEnum(String status) {
        switch (status) {
            case "TODO":
                return TODO;
            case "IN PROGRESS":
                return IN_PROGRESS;
            case "DONE":
                return DONE;
            default:
                return null;
        }
    }
    
    private String value;

    EStatus() {
        this.value = this.name();
    }

    EStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}