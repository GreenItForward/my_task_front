package com.mytask.front.service;

import com.mytask.front.utils.EString;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertService {
    public static ButtonType showAlertConfirmation(EAlertType confirmation, String title, String body) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(confirmation.name());
        alert.setHeaderText(body);
        alert.setContentText(EString.ALERT_VERIFICATION.getString());

        return alert.showAndWait().orElse(ButtonType.CANCEL);
    }

    public static boolean isConfirmed(ButtonType buttonType) {
        return buttonType == ButtonType.OK;
    }


    public enum EAlertType {
        ERROR,
        WARNING,
        INFO,
        CONFIRMATION
    }


}