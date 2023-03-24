package com.mytask.front;

import com.mytask.front.controller.CreateTabController;
import com.mytask.front.controller.IndexController;
import com.mytask.front.controller.ShowTabController;
import com.mytask.front.model.EPage;
import com.mytask.front.service.ScreenService;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ScreenService screenService = new ScreenService(stage);

        // Charger les écrans
        screenService.loadScreen(EPage.INDEX, () -> new IndexController(screenService));
        screenService.loadScreen(EPage.CREATE_TAB, () -> new CreateTabController(screenService));
        screenService.loadScreen(EPage.SHOW_TAB, () -> new ShowTabController(screenService));
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/mytask/front/icons/gif.png"))));

        // Configurer la scène initiale
        EPage initialPage = EPage.INDEX;
        stage.setTitle(initialPage.getWindowTitle());
        stage.setScene(new Scene((Parent) screenService.screens.get(initialPage), initialPage.getWidth(), initialPage.getHeight()));
        stage.centerOnScreen();
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) { launch(); }

}