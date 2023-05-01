package com.mytask.front.utils.enums;

public enum EExportType {
    PDF("pdf"),
    CSV("csv"),
    JSON("json");

    private final String type;

    EExportType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
