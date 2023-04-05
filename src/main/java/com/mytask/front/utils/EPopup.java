package com.mytask.front.utils;

import java.net.URL;

public enum EPopup {
    MEMBERS("members", "Gestion des membres", 420, 500),
    INVITE_CODE("invite_code", "Gestion des codes d'invitation", 420, 500),
    TASK_DETAILS("task_details_popup", "Editer la tâche", 700, 525),
    TABLE_LIST("tables_list_popup", "Liste des tableaux", 420, 500);

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

    public URL getFxmlPath() {
        return getClass().getResource(EPath.FXML.getPath() + fxmlName + ".fxml");
    }
}
