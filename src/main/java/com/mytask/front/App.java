package com.mytask.front;

import com.mytask.front.controller.*;
import com.mytask.front.service.AppService;
import com.mytask.front.service.view.AlertService;
import com.mytask.front.service.view.PopupService;
import com.mytask.front.service.view.TabService;
import com.mytask.front.utils.enums.EPage;
import com.mytask.front.service.view.ScreenService;
import javafx.application.Application;
import javafx.scene.control.ButtonType;
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

        // Configurer la scène initiale
        EPage initialPage = EPage.CONNECTION;
        screenService.configureInitialScreen(initialPage);

        AppService.setListenerOnStage(stage);
        stage.show();
    }

    public static void main(String[] args) { launch(); }

}