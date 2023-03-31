package com.mytask.front.service;

import com.mytask.front.App;
import com.mytask.front.utils.EPage;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ScreenService {
    private static ScreenService instance;

    public final Map<EPage, Node> screens = new HashMap<>();
    private final Stage stage;

    private ScreenService(Stage stage) {
        this.stage = stage;
    }

    // Singleton
    public static ScreenService getInstance(Stage stage) {
        if (instance == null) {
            instance = new ScreenService(stage);
        }
        return instance;
    }

    
    public void loadScreen(EPage page, Supplier<?> controllerSupplier) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/mytask/front/view/" + page.getFxmlName() + ".fxml"));
            fxmlLoader.setControllerFactory(param -> controllerSupplier.get());
            Parent root = fxmlLoader.load();
            screens.put(page, root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setScreen(EPage page) {
        Node screen = screens.get(page);
        if (screen != null) {
            stage.setTitle(page.getWindowTitle());
            stage.getScene().setRoot((Parent) screen);
            stage.setWidth(page.getWidth());
            stage.setHeight(page.getHeight());
            stage.centerOnScreen();
        } else {
            throw new IllegalArgumentException("Screen not found: " + page);
        }
    }

    public void setPopupScreen(Stage primaryStage, EPage page) {
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
        Scene popupScene = new Scene(layout, 300, 200);
        popup.setScene(popupScene);
        popup.showAndWait();
    }


}
