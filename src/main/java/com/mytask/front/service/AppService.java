package com.mytask.front.service;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.paint.Color;

public class AppService {
    public static String colorToHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public static void listenerDisableButton(Object object, Button button) {
        if (object instanceof TextField) {
            TextField textField = (TextField) object;
            addListenerToTextInputControl(textField, button);
        } else if (object instanceof TextArea) {
            TextArea textArea = (TextArea) object;
            addListenerToTextInputControl(textArea, button);
        } else {
            throw new IllegalArgumentException("object must be an instance of TextField or TextArea");
        }
    }

    private static void addListenerToTextInputControl(TextInputControl textInputControl, Button button) {
        textInputControl.textProperty().addListener((observable, oldValue, newValue) -> {
            button.setDisable(newValue.isEmpty());
        });
    }

}
