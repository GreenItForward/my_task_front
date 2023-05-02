package com.mytask.front.utils.enums;

public enum EStyle {
    STYLES("styles.css"),
    SHOW_TAB("show_tab.css"),
    INDEX("index.css"),
    POPUP("popup.css"),
    CREATE_INVITE_CODE("create_invite_code.css"),
    AUTH("auth.css");

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
