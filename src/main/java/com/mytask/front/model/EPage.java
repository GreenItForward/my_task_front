package com.mytask.front.model;

public enum EPage {
    INDEX("index", "MyTask", 1280, 720),
    CREATE_TAB("create_tab", "Create Task", 1280, 720);

    private String fxmlName;
    private String windowTitle;

    private int width;

    private int height;

    EPage(String fxmlName, String windowTitle, int width, int height) {
        this.fxmlName = fxmlName;
        this.windowTitle = windowTitle;
        this.width = width;
        this.height = height;
    }

    public String getFxmlName() {
        return fxmlName;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
