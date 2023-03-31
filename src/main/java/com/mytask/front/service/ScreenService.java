package com.mytask.front.service;

import com.mytask.front.App;
import com.mytask.front.utils.EIcon;
import com.mytask.front.utils.EPage;
import com.mytask.front.utils.EPath;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class ScreenService {
    private static ScreenService instance;

    public final Map<EPage, Node> screens = new EnumMap<>(EPage.class);
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

    
    public void loadScreen(EPage page, Supplier<?> controllerSupplier) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(EPath.FXML.getPath() + page.getFxmlName()));
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


    public void configureInitialScreen(EPage initialPage) {
        stage.setTitle(initialPage.getWindowTitle());
        stage.setScene(new Scene((Parent) this.screens.get(initialPage), initialPage.getWidth(), initialPage.getHeight()));
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(EIcon.GIF.getImagePath()))));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }


}
