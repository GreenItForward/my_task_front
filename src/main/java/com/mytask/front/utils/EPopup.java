package com.mytask.front.utils;

public enum EPopup {
    MEMBERS("members", "Gestion des membres", 300, 200);

    private String fxmlName;
    private String windowTitle;
    private int width;
    private int height;

    EPopup(String fxmlName, String windowTitle, int width, int height) {
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
