package com.mytask.front.utils;

import javafx.stage.Stage;

public enum EPage {
    SHOW_ALL_TAB("show_all_tab", "MyTask - GreenItForward", 1280, 720),
    SHOW_TAB("show_tab", "MyTask - GreenItForward", 1880, 720),
    INDEX("index", "MyTask - GreenItForward", 400, 400),
    CREATE_TAB("create_tab", "MyTask - GreenItForward", 1280, 720),
    CONNECTION("connection", "MyTask - GreenItForward", 1280, 720),
    INSCRIPTION("inscription", "MyTask - GreenItForward", 1280, 720),
    MEMBERS("members", "Gestion des membres", 300, 200);


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
        return fxmlName + ".fxml";
    }

    public String getWindowTitle() {
        if (windowTitle.isEmpty()) {
            return EString.getCompleteNameApp();
        }

        return windowTitle;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }
}