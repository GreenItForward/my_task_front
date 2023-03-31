package com.mytask.front.utils;

public enum EPath {
    FXML("/com/mytask/front/view/"),
    CSS("/com/mytask/front/style/"),
    ICONS("/com/mytask/front/icons/");

    private String path;

    EPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
