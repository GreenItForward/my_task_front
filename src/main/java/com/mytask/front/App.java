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
        screenService.loadScreen(EPage.CONNECTION, () -> new ConnectionController(screenService));
        screenService.loadScreen(EPage.INSCRIPTION, () -> new InscriptionController(screenService));
        screenService.loadScreen(EPage.INDEX, () -> new IndexController(screenService));
        screenService.loadScreen(EPage.CREATE_TAB, () -> new CreateTabController(screenService));
        screenService.loadScreen(EPage.SHOW_ALL_TAB, () -> new ShowAllTabController(screenService));
        screenService.loadScreen(EPage.SHOW_TAB, () -> new ShowTabController(screenService));
        screenService.loadScreen(EPage.TASK_DETAILS, () -> new TaskDetailsController(screenService));

        // Configurer la scène initiale
        EPage initialPage = EPage.CONNECTION;
        screenService.configureInitialScreen(initialPage);
        stage.show();
    }

    public static void main(String[] args) { launch(); }

}