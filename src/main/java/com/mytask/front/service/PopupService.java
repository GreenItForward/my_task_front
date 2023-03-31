package com.mytask.front.service;

import com.mytask.front.utils.EIcon;
import com.mytask.front.utils.EPage;
import com.mytask.front.utils.EPath;
import com.mytask.front.utils.EPopup;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Objects;

public class PopupService {

    private PopupService() {
        throw new IllegalStateException("Utility class");
    }

    public static void setPopupScreen(Stage primaryStage, EPopup page) {
        Stage popup = new Stage();
        popup.initModality(Modality.WINDOW_MODAL);
        popup.initOwner(primaryStage);
        popup.setTitle(page.getWindowTitle());

        Label label = new Label("Modifier les droits ou supprimer les utilisateurs");
        Button closeButton = new Button("Fermer");
        closeButton.setOnAction(e -> popup.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene popupScene = new Scene(layout, EPopup.MEMBERS.getWidth(), EPopup.MEMBERS.getHeight());
        popup.setScene(popupScene);
        popup.getIcons().add(new Image(Objects.requireNonNull(PopupService.class.getResourceAsStream(EIcon.GIF.getImagePath()))));
        popup.centerOnScreen();
        popup.setResizable(false);
        popup.showAndWait();
    }
}
