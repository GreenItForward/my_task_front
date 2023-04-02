package com.mytask.front.utils;

import javafx.scene.image.Image;

import java.util.Objects;

public enum EIcon {
    CLOCK_ICON("clock.png"),
    CLOCK_ICON_CHECKED("check.png"),
    PENCIL_ICON("pencil.png"),
    GIF("gif.png");

    private final String imagePath;

    EIcon(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return EPath.ICONS.getPath() + imagePath;
    }

    public Image getImage() {
        return new Image(Objects.requireNonNull(EIcon.class.getResourceAsStream(getImagePath())));
    }
}
