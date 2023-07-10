package com.mytask.front.utils.enums;

public enum EPage {
    SHOW_ALL_TAB("show_all_tab", "", 1280, 720),
    SHOW_TAB("show_tab", "", 1280, 720),
    INDEX("index", "", 400, 400),
    CREATE_TAB("create_tab", "", 1280, 720),
    CONNECTION("connection", "", 1280, 720),
    INSCRIPTION("inscription", "", 1280, 720),
    PARAM("param", "", 1280, 720);


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