package com.mytask.front.service;

import com.mytask.front.App;
import com.mytask.front.model.EPage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ScreenService {
    public final Map<EPage, Node> screens = new HashMap<>();
    private final Stage stage;

    public ScreenService(Stage stage) {
        this.stage = stage;
    }

    public void loadScreen(EPage page, Supplier<?> controllerSupplier) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/mytask/front/" + page.getFxmlName() + ".fxml"));
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
}
