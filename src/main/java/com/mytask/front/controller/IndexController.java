package com.mytask.front.controller;
import com.mytask.front.model.EPage;
import com.mytask.front.service.ScreenService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class IndexController {

    @FXML
    private Button voir_tableau;

    @FXML
    private Button creer_tableau;

    @FXML
    private Button quitter;

    private final ScreenService screenService;

    public IndexController(ScreenService screenService) {
        this.screenService = screenService;
    }

    public void initialize() {
        voir_tableau.setOnAction(event -> {
            System.out.println("Voir tableau");
            // envoie vers la page contenant tout les tableau
        });

        creer_tableau.setOnAction(event -> {
            System.out.println("Creer tableau");
            screenService.setScreen(EPage.CREATE_TAB);
        });

        quitter.setOnAction(event -> {
            Platform.exit();
        });
    }
}