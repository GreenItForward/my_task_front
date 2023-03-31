package com.mytask.front.utils;

public enum EStyle {
    STYLES("styles.css"),
    SHOW_TAB("show_tab.css"),
    INDEX("index.css"),
    POPUP("popup.css");

    private String cssName;

    EStyle(String cssName) {
        this.cssName = cssName;
    }

    public String getCssName() {
        return cssName;
    }

    public String getCssPath() {
        return EPath.CSS.getPath() + cssName;
    }
}