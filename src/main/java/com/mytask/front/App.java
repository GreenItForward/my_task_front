package com.mytask.front;

import com.mytask.front.controller.*;
import com.mytask.front.service.view.TabService;
import com.mytask.front.utils.EPage;
import com.mytask.front.service.view.ScreenService;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        ScreenService screenService = ScreenService.getInstance(stage);
        TabService.init(stage);

        // Charger les écrans (sauf index qui est chargé pendant la connexion)
        screenService.loadScreen(EPage.CONNECTION, ConnectionController::new);
        screenService.loadScreen(EPage.INSCRIPTION, InscriptionController::new);
        screenService.loadScreen(EPage.CREATE_TAB, CreateTabController::new);
        screenService.loadScreen(EPage.SHOW_ALL_TAB, ShowAllTabController::new);
        screenService.loadScreen(EPage.SHOW_TAB, ShowTabController::getInstance);

        // Configurer la scène initiale
        EPage initialPage = EPage.CONNECTION;
        screenService.configureInitialScreen(initialPage);
        stage.show();
    }

    public static void main(String[] args) { launch(); }

}