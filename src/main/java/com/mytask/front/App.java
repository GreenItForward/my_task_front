package com.mytask.front;

import com.mytask.front.controller.*;
import com.mytask.front.service.TabService;
import com.mytask.front.utils.EPage;
import com.mytask.front.service.ScreenService;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ScreenService screenService = ScreenService.getInstance(stage);
        TabService.init(stage);

        // Charger les écrans
        screenService.loadScreen(EPage.CONNECTION, ConnectionController::new);
        screenService.loadScreen(EPage.INSCRIPTION, InscriptionController::new);
        screenService.loadScreen(EPage.INDEX, IndexController::new);
        screenService.loadScreen(EPage.CREATE_TAB, CreateTabController::new);
        screenService.loadScreen(EPage.SHOW_ALL_TAB, ShowAllTabController::new);
        screenService.loadScreen(EPage.SHOW_TAB, ShowTabController::new);

        // Configurer la scène initiale
        EPage initialPage = EPage.CONNECTION;
        screenService.configureInitialScreen(initialPage);
        stage.show();
    }

    public static void main(String[] args) { launch(); }

}